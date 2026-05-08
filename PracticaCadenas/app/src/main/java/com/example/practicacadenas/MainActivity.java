package com.example.practicacadenas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private EditText inputPalabra;
    private TextView resPalindromo, resVocales, resConsonantes, resPenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPalabra = findViewById(R.id.Palabra);
        resPalindromo = findViewById(R.id.resPalindromo);
        resVocales = findViewById(R.id.resVocales);
        resConsonantes = findViewById(R.id.resConsonante);
        resPenta = findViewById(R.id.resPentavocalica);


        findViewById(R.id.btnPalindromo).setOnClickListener(v -> {
            String texto = inputPalabra.getText().toString().toLowerCase();
            boolean esPalindromo = true;

            int izquierda = 0;
            int derecha = texto.length() - 1;

            while (izquierda < derecha) {
                if (texto.charAt(izquierda) != texto.charAt(derecha)) {
                    esPalindromo = false;
                    break;
                }
                izquierda++;
                derecha--;
            }

            if (esPalindromo && texto.length() > 0) resPalindromo.setText("Sí");
            else resPalindromo.setText("No");
        });

        findViewById(R.id.btnVocales).setOnClickListener(v -> {
            String texto = inputPalabra.getText().toString().toLowerCase();
            int contador = 0;

            for (int i = 0; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                    contador++;
                }
            }
            resVocales.setText("Cant: " + contador);
        });

        findViewById(R.id.btnConsonantes).setOnClickListener(v -> {
            String texto = inputPalabra.getText().toString().toLowerCase();
            int contador = 0;

            for (int i = 0; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if ((c >= 'a' && c <= 'z') && !(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')) {
                    contador++;
                }
            }
            resConsonantes.setText("Cant: " + contador);
        });

        findViewById(R.id.btnPentavocalica).setOnClickListener(v -> {
            String texto = inputPalabra.getText().toString().toLowerCase();
            boolean tieneA = false, tieneE = false, tieneI = false, tieneO = false, tieneU = false;

            for (int i = 0; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if (c == 'a') tieneA = true;
                if (c == 'e') tieneE = true;
                if (c == 'i') tieneI = true;
                if (c == 'o') tieneO = true;
                if (c == 'u') tieneU = true;
            }

            if (tieneA && tieneE && tieneI && tieneO && tieneU) {
                resPenta.setText("¡Sí!");
            } else {
                resPenta.setText("No");
            }
        });
    }
}