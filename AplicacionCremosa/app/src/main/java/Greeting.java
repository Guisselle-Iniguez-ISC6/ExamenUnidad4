package com.example.aplicacioncremosa;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Greeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtSaludo = (TextView) findViewById(R.id.TxtSaludo);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            txtSaludo.setText("Hola " + bundle.getString("NOMBRE"));
        }
    }
}