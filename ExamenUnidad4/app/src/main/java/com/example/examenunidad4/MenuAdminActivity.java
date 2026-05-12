package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_admin);
    }

    public void abrirAlumnos(View v) {
        Intent intent = new Intent(this, Alumnos.class);
        startActivity(intent);
    }

    public void abrirMaterias(View v) {
        Intent intent = new Intent(this, Materias.class);
        startActivity(intent);
    }

    public void cerrarSesion(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}