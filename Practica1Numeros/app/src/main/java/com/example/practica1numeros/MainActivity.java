package com.example.practica1numeros;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtSaludo = (TextView) findViewById(R.id.TxtSaludo);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        }


        Button btnPrimos = findViewById(R.id.btnPrimos);
        Button btnPerfectos = findViewById(R.id.btnPerfectos);
        Button btnAmigos = findViewById(R.id.btnAmigos);

        btnPrimos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NumerosPrimos.class);
                startActivity(intent);
            }
        });


        btnPerfectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NumerosPerfectos.class);
                startActivity(intent);
            }
        });


        btnAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NumeroMagico.class);
                startActivity(intent);
            }
        });
    }

}

