package com.example.practica1numeros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // Importación necesaria
import android.widget.EditText; // Importación necesaria
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NumerosPrimos extends AppCompatActivity {

    // 1. Declaramos las variables globales para los controles
    private EditText editNumero;
    private Button btnCalcula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numerosprimos);

        // Referencias existentes
        TextView txtSaludo = (TextView) findViewById(R.id.TxtSaludo);
        ImageView flechaIzquierda = findViewById(R.id.flechaizq);
        ImageView flechaDerecha = findViewById(R.id.flechadere);

        // 2. Inicializamos nuestros nuevos controles
        editNumero = findViewById(R.id.editNumero);
        btnCalcula = findViewById(R.id.btnCalcula);

        // Lógica del saludo
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        }

        // --- Lógica para Calcular Número Primo ---
        btnCalcula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editNumero.getText().toString();

                if (!input.isEmpty()) {
                    int numero = Integer.parseInt(input);
                    if (esPrimo(numero)) {
                        Toast.makeText(NumerosPrimos.this, "¡El " + numero + " es PRIMO! ✅", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NumerosPrimos.this, "El " + numero + " NO es primo ❌", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NumerosPrimos.this, "Por favor, ingresa un número", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navegación (flechas)
        flechaIzquierda.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        });

        flechaDerecha.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NumerosPerfectos.class);
            startActivity(intent);
        });
    }

    // 3. Método lógico para verificar si es primo
    private boolean esPrimo(int n) {
        if (n <= 1) return false; // 0 y 1 no son primos
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}