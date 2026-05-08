package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerRol;
    private EditText etPassLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerRol = findViewById(R.id.spinnerRol);
        etPassLogin = findViewById(R.id.etPassLogin);
    }

    public void validarLogin(View v) {
        String rol = spinnerRol.getSelectedItem().toString();
        String password = etPassLogin.getText().toString();

        if (password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu clave", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (rol) {
            case "Administrador":
                if (password.equals("123456")) {
                    irAFormulario();
                } else {
                    Toast.makeText(this, "Clave de Admin incorrecta", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Docente":
                // 2 letras y 8 números
                if (password.matches("^[a-zA-Z]{2}[0-9]{8}$")) {
                    irAFormulario();
                } else {
                    Toast.makeText(this, "Docente: Requiere 2 letras y 8 números", Toast.LENGTH_LONG).show();
                }
                break;

            case "Estudiante":
                // 1 letra y 8 números
                if (password.matches("^[a-zA-Z]{1}[0-9]{8}$")) {
                    irAFormulario();
                } else {
                    Toast.makeText(this, "Estudiante: Requiere 1 letra y 8 números", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void irAFormulario() {
        Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
        // Cambia 'Alumnos.class' por el nombre de tu actividad del CRUD
        Intent intent = new Intent(this, Alumnos.class);
        startActivity(intent);
        finish(); // Cerramos el login para que no puedan volver atrás con el botón físico
    }
}