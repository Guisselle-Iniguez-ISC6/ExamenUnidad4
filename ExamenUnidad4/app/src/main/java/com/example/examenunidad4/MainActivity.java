package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etPassLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPassLogin = findViewById(R.id.etPassLogin);
    }

    public void validarLogin(View v) {

        String usuario = etUsuario.getText().toString().trim();
        String password = etPassLogin.getText().toString().trim();

        if (usuario.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // CONTAR LETRAS DEL USUARIO
        int letras = contarLetras(usuario);

        // ADMIN -> SOLO NÚMEROS
        if (letras == 0) {

            Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Admin.class);
            startActivity(intent);
            finish();
        }

        // ALUMNO -> 1 LETRA
        else if (letras == 1) {

            Toast.makeText(this, "Bienvenido Alumno", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Alumnos.class);
            startActivity(intent);
            finish();
        }

        // DOCENTE -> 2 LETRAS
        else if (letras == 2) {

            Toast.makeText(this, "Bienvenido Docente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Docente.class);
            startActivity(intent);
            finish();
        }

        else {

            Toast.makeText(this, "Usuario inválido", Toast.LENGTH_SHORT).show();
        }
    }

    // MÉTODO PARA CONTAR LETRAS
    private int contarLetras(String texto) {

        int letras = 0;

        for (int i = 0; i < texto.length(); i++) {

            char c = texto.charAt(i);

            if (Character.isLetter(c)) {
                letras++;
            }
        }

        return letras;
    }
}