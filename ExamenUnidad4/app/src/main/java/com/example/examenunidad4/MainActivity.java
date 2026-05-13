package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etPassLogin;
    private Spinner spinnerRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPassLogin = findViewById(R.id.etPassLogin);
        spinnerRol = findViewById(R.id.spinnerRol);
    }

    public void validarLogin(View v) {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassLogin.getText().toString().trim();
        String rol = spinnerRol.getSelectedItem().toString();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rol.equals("Administrador")) {
            if (password.equals("123456")) {
                startActivity(new Intent(this, MenuAdminActivity.class));
            } else {
                Toast.makeText(this, "Clave de Admin incorrecta", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String rolDB = rol.equals("Estudiante") ? "Alumno" : "Maestra/Docente";

        Cursor fila = bd.rawQuery("SELECT nombre FROM usuarios WHERE nombre=? AND password=? AND tipo=?",
                new String[]{usuario, password, rolDB});

        if (fila.moveToFirst()) {
            if (rol.equals("Docente")) {
                Intent i = new Intent(this, DocenteActivity.class);
                i.putExtra("nombre_usuario", usuario);
                startActivity(i);
            } else {
                Intent i = new Intent(this, AlumnoActivity.class);
                i.putExtra("nombre_usuario", usuario);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "Usuario, contraseña o rol incorrectos", Toast.LENGTH_SHORT).show();
        }
        fila.close();
        bd.close();
    }
}