package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Materias extends AppCompatActivity {

    private EditText etNombreMat, etClaveMat;
    private Spinner spinnerDocentes;
    private ArrayList<String> listaDocentes;
    private ArrayList<String> listaIdsDocentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_materias);

        etNombreMat = findViewById(R.id.etNombreMateria);
        etClaveMat = findViewById(R.id.etClaveMateria);
        spinnerDocentes = findViewById(R.id.spinnerDocentes);

        cargarDocentes();
    }

    private void cargarDocentes() {

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        listaDocentes = new ArrayList<>();
        listaIdsDocentes = new ArrayList<>();

        Cursor fila = bd.rawQuery(
                "SELECT nombre FROM usuarios WHERE tipo='Maestra/Docente'",
                null
        );

        if (fila.moveToFirst()) {

            do {
                String nombreDocente = fila.getString(0);
                listaIdsDocentes.add(nombreDocente);
                listaDocentes.add(nombreDocente);

            } while (fila.moveToNext());

        } else {

            listaDocentes.add("No hay docentes registrados");
        }

        fila.close();
        bd.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaDocentes
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerDocentes.setAdapter(adapter);
    }

    public void registrarMateria(View v) {

        String nombre = etNombreMat.getText().toString().trim();
        String clave = etClaveMat.getText().toString().trim();

        if (nombre.isEmpty() || clave.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (listaDocentes.size() == 0 ||
                listaDocentes.get(0).equals("No hay docentes registrados")) {

            Toast.makeText(
                    this,
                    "No hay docentes registrados",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int posicion = spinnerDocentes.getSelectedItemPosition();
        String docenteNombre = listaIdsDocentes.get(posicion);

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();

        registro.put("clave", clave);
        registro.put("nombre_materia", nombre);
        registro.put("docente_asignado", docenteNombre);

        long resultado = bd.insert(
                "materias",
                null,
                registro
        );

        if (resultado != -1) {

            Toast.makeText(
                    this,
                    "Materia registrada correctamente",
                    Toast.LENGTH_SHORT
            ).show();

            etNombreMat.setText("");
            etClaveMat.setText("");

        } else {

            Toast.makeText(
                    this,
                    "Error al registrar. La clave podría estar duplicada.",
                    Toast.LENGTH_SHORT
            ).show();
        }

        bd.close();
    }
}
