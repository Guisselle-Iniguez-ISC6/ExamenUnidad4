package com.example.aplicacioncremosa;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText txtNombre = (EditText) findViewById(R.id.TxtNombre);
        final Button btnHola = (Button) findViewById(R.id.BtnSaludo);
        final ImageButton btnMeses = (ImageButton) findViewById(R.id.BtnMeses);
        final EditText txtEdad = (EditText) findViewById(R.id.TxtEdad);
        final EditText txtMeses = (EditText) findViewById(R.id.TxtMeses);

        btnMeses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String edad = txtEdad.getText().toString();
                if (!edad.isEmpty()) {
                    int ed = Integer.parseInt(edad);
                    String mes = Integer.toString(ed * 12);
                    txtMeses.setText(mes);
                }
            }
        });

        btnHola.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Greeting.class);
                Bundle b = new Bundle();
                b.putString("NOMBRE", txtNombre.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}