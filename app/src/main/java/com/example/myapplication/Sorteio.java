package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Sorteio extends AppCompatActivity {
    private androidx.appcompat.widget.AppCompatButton contador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteio);

        Sorteio();
    }

    private void Sorteio(){

        Calendar c = Calendar.getInstance();
        Date dataAtual = c.getTime();
        long dtAtualMillis = dataAtual.getTime(); // data atual em millisegundos

        c.set(Calendar.DAY_OF_MONTH, 26 );
        c.set(Calendar.HOUR, Calendar.MARCH);
        c.set(Calendar.YEAR, 2023 );
        Date dataSorteio = c.getTime();
        long dtSorteioMillis = dataSorteio.getTime();  // data do sorteio em millisegundos

//        Date result = new Date( dtSorteioMillis - dtAtualMillis );
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String StrResult = dateFormat.format(result);

        contador = findViewById(R.id.contador);
        if (dtAtualMillis > dtSorteioMillis) {
            contador.setText("Sorteio ja realizado");

        }else {

            //buscar alguem cadastrado e sortear

//            contador.setText();
        }
    }
}