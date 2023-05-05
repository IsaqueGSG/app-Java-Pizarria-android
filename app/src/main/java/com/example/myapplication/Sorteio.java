package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.checkerframework.checker.units.qual.A;

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
    private androidx.appcompat.widget.AppCompatButton PainelResultado;
    private TextView btDeslogar;
    private ListView listView;
    private ArrayList<String> lista;
    private ArrayAdapter<String> arrayAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio);

        getSupportActionBar().hide();
        initComponents();

        btDeslogar.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Sorteio.this, FormLogin.class);
            startActivity(intent);
            finish();
        });

        Sortear();
        arrayView();
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

    private Date dtAtual(){
        Calendar c = Calendar.getInstance();
        Date dataAtual = c.getTime();
        return  dataAtual;
    }

    private Date dtSorteio(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 30 );
        c.set(Calendar.HOUR, Calendar.MARCH);
        c.set(Calendar.YEAR, 2023 );
        Date dataSorteio = c.getTime();
        return dataSorteio;
    };

    private void Sortear(){

        if ( dtAtual().getTime() == dtSorteio().getTime() ){
            Sortear();
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String StrDataSorteio = dateFormat.format(dtSorteio());
            PainelResultado.setText("data do sorteio " + StrDataSorteio);
        }

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
                                PainelResultado.setText("Você Ganhou!!");

                            }else{
                                System.out.println( "voce nao ganhou "  + CurrentUserID + " id soretado "+ IdSorteado  );
                                PainelResultado.setText("Não foi dessa vez");
                            }
                        }
                    }
                });

            }

    private ArrayList<String> preencherDados(){
        ArrayList<String> dados = new ArrayList<String>();
        dados.add("isaque");
        return dados;
    };

    private void arrayView(){
        lista = preencherDados();
        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(Sorteio.this, "clicado", Toast.LENGTH_SHORT).show();
            }
        });
    }


            private void initComponents(){
                PainelResultado = findViewById(R.id.contador);
                btDeslogar = (TextView) findViewById(R.id.btDeslogar);
                listView = findViewById(R.id.lista);
            }
}