package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class FormCadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_senha;
    private Button bt_cadastrar;

    String[] mensagens = {"preencha todos os campos", "Cadastro realizado com sucesso", "erro no cadastro"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        initComponents();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Editable nomeAUX = edit_nome.getText();
                Editable emailAUX = edit_email.getText();
                Editable sennaAUX = edit_senha.getText();

                String nome = nomeAUX.toString();
                String email = emailAUX.toString();
                String senha = sennaAUX.toString();



                if ( nome.isEmpty() || senha.isEmpty() || email.isEmpty() ){
                    Snackbar snackbar = Snackbar.make( v ,mensagens[0], Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    CadastrarUsuario(v);
                }
            }
        });
    }


    private void CadastrarUsuario(View v){

        Editable nomeAUX = edit_nome.getText();
        Editable emailAUX = edit_email.getText();
        Editable senhaAUX = edit_senha.getText();

        String nome = nomeAUX.toString();
        String email = emailAUX.toString();
        String senna = senhaAUX.toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senna).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Snackbar snackbar = Snackbar.make( v ,mensagens[1], Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make( v ,mensagens[2], Snackbar.LENGTH_SHORT );
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void initComponents(){

        edit_nome = (EditText) findViewById(R.id.edit_nome);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_senha = (EditText) findViewById(R.id.edit_senha);
        bt_cadastrar = findViewById(R.id.bt_cadastrar);
    }

}