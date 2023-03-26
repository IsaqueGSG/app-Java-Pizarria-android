package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Sorteio extends AppCompatActivity {
    private androidx.appcompat.widget.AppCompatButton contador;
    private TextView btDeslogar;
    private TextView btSortear;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio);

        getSupportActionBar().hide();

        contador = findViewById(R.id.contador);

        btDeslogar = (TextView) findViewById(R.id.btDeslogar);
        btDeslogar.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Sorteio.this, FormLogin.class);
            startActivity(intent);
            finish();
        });

        btSortear = (TextView) findViewById(R.id.sortear);
        btSortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sortear(contador);
            }
        });

        DataSorteio();
    }

    protected void onStart() {
        super.onStart();

        TextView nomeUsuario = findViewById(R.id.saudacao);
        String usuarioID;

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference referencia = db.collection("Usuarios").document(usuarioID);
        referencia.addSnapshotListener((documentSnapshot, error) -> {
            if(documentSnapshot != null){
                nomeUsuario.setText("Olá "+ documentSnapshot.getString("nome"));
            }
        });
    }

    private void Sortear(androidx.appcompat.widget.AppCompatButton contador){
        db.collection("Usuarios")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            StringBuffer s = new StringBuffer();
                            for(QueryDocumentSnapshot document : task.getResult()  ){
                                s.append( document.getId().toString() + "," );
                            }

                            String[] IdUsuarios = s.toString().split(",");
                            Random random = new Random();
                            int indice = random.nextInt( IdUsuarios.length );
                            String IdSorteado = IdUsuarios[indice];

                            String CurrentUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            if( CurrentUserID.equals(IdSorteado) ) {
                                System.out.println( "voce ganhou " + CurrentUserID + " id soretado "+ IdSorteado  );
                                contador.setText("Você Ganhou!!");

                            }else{
                                System.out.println( "voce nao ganhou "  + CurrentUserID + " id soretado "+ IdSorteado  );
                                contador.setText("Não foi dessa vez");
                            }

                        }
                    }
                });
    }

    private void DataSorteio(){

        Calendar c = Calendar.getInstance();
        Date dataAtual = c.getTime();
        long dtAtualMillis = dataAtual.getTime(); // data atual em millisegundos

        c.set(Calendar.DAY_OF_MONTH, 30 );
        c.set(Calendar.HOUR, Calendar.MARCH);
        c.set(Calendar.YEAR, 2023 );
        Date dataSorteio = c.getTime();
        long dtSorteioMillis = dataSorteio.getTime();  // data do sorteio em millisegundos

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String StrDataSorteio = dateFormat.format(dataSorteio);


        if (dtAtualMillis == dtSorteioMillis) {
            Sortear(contador);

        }else{
            contador.setText("data do sorteio " + StrDataSorteio);
        }
    }
}