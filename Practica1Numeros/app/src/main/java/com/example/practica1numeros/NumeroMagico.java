package com.example.practica1numeros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NumeroMagico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numeromagico);

        // 1. Enlaces con el XML
        TextView TxtTitulo = findViewById(R.id.TxtTitulo);
        EditText primerNumero = findViewById(R.id.primerNumero);
        EditText segundoNumero = findViewById(R.id.segundoNumero);
        Button btnCalcula = findViewById(R.id.btnCalcula);
        ImageView flechaIzquierda = findViewById(R.id.flechaizq);
        ImageView flechaDerecha = findViewById(R.id.flechadere);

        // 2. Lógica del saludo (Recibiendo el nombre desde el Intent)
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            TxtTitulo.setText("Hola " + bundle.getString("NOMBRE"));
        }

        // 3. Lógica del Botón "¿Son Amigos?"
        btnCalcula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n1Str = primerNumero.getText().toString();
                String n2Str = segundoNumero.getText().toString();

                // Validamos que no estén vacíos
                if (!n1Str.isEmpty() && !n2Str.isEmpty()) {
                    int n1 = Integer.parseInt(n1Str);
                    int n2 = Integer.parseInt(n2Str);

                    // Verificamos la amistad de los números
                    if (sonAmigos(n1, n2)) {
                        Toast.makeText(NumeroMagico.this, "¡Son Números Amigos! ✨", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NumeroMagico.this, "No son amigos ❌", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NumeroMagico.this, "Escribe ambos números", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 4. Navegación (Flechas) - Respetando tu código original
        flechaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        flechaDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // --- Métodos Matemáticos ---

    // Función para saber si dos números son amigos
    private boolean sonAmigos(int a, int b) {
        return (sumaDivisores(a) == b && sumaDivisores(b) == a);
    }

    // Función para obtener la suma de los divisores propios
    private int sumaDivisores(int n) {
        int suma = 0;
        // Buscamos divisores desde 1 hasta la mitad del número
        for (int i = 1; i <= n / 2; i++) {
            if (n % i == 0) {
                suma += i;
            }
        }
        return suma;
    }
}