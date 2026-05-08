package com.example.practica1numeros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;    // Agregado
import android.widget.EditText;  // Agregado
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;     // Agregado
import androidx.appcompat.app.AppCompatActivity;

public class NumerosPerfectos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numerosperfectos);

        // Enlazar componentes del XML
        TextView txtSaludo = findViewById(R.id.TxtSaludo);
        EditText editNumero = findViewById(R.id.editNumero);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        ImageView flechaIzquierda = findViewById(R.id.flechaizq);
        ImageView flechaDerecha = findViewById(R.id.flechadere);

        // Lógica del saludo
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        }

        // --- Lógica del Botón ¿Es Perfecto? ---
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = editNumero.getText().toString();

                if (!texto.isEmpty()) {
                    int num = Integer.parseInt(texto);
                    if (esPerfecto(num)) {
                        Toast.makeText(NumerosPerfectos.this, num + " es Perfecto ✨", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NumerosPerfectos.this, num + " no es Perfecto ❌", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NumerosPerfectos.this, "Escribe un número", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navegación
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
                Intent intent = new Intent(v.getContext(), NumeroMagico.class);
                startActivity(intent);
            }
        });
    }

    // Método matemático para Números Perfectos
    public boolean esPerfecto(int numero) {
        if (numero <= 1) return false;
        int sumaDivisores = 0;

        // Sumamos todos los divisores menores al número
        for (int i = 1; i < numero; i++) {
            if (numero % i == 0) {
                sumaDivisores += i;
            }
        }
        // Es perfecto si la suma es igual al número (ej: 6 = 1+2+3)
        return sumaDivisores == numero;
    }
}