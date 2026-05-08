package com.example.examenuni2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputNumero;
    TextView resOportunidades, resResultado, resRespuesta;
    int alea;
    int oportunidades = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumero = findViewById(R.id.Numero);
        resOportunidades = findViewById(R.id.resOportunidades);
        resRespuesta = findViewById(R.id.resRespuesta);
        resResultado = findViewById(R.id.resResultado);

        nuevoJuego();

        findViewById(R.id.btnJugar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strNumero = inputNumero.getText().toString();

                if (strNumero.equals("")) {
                    resRespuesta.setText("Introduce un número");
                    return;
                }

                int numero = Integer.parseInt(strNumero);

                if (numero == alea) {
                    resResultado.setText("GANASTE");
                    resRespuesta.setText("El número era " + alea);
                    oportunidades = 0;
                } else {

                    oportunidades--;
                    resOportunidades.setText("Te quedan " + oportunidades + " oportunidades");

                    if (numero > alea) {
                        resRespuesta.setText("El número es menor");
                    } else {
                        resRespuesta.setText("El número es mayor");
                    }

                    if (oportunidades == 0) {
                        resResultado.setText("PERDISTE. El número era: " + alea);
                    }
                }
            }
        });

        findViewById(R.id.btnLimpiar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputNumero.setText("");
                resOportunidades.setText("");
                resRespuesta.setText("");
                resResultado.setText("");

                nuevoJuego();
            }
        });
    }

    void nuevoJuego() {
        alea = (int) (Math.random() * 10);
        oportunidades = 5;
        resOportunidades.setText("Oportunidades: " + oportunidades);
    }
}