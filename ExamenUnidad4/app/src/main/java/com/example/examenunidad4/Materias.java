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

    // Lista de nombres para el Spinner
    private ArrayList<String> listaDocentes;

    // Lista de IDs reales de docentes
    private ArrayList<Integer> listaIdsDocentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_materias);

        etNombreMat = findViewById(R.id.etNombreMateria);
        etClaveMat = findViewById(R.id.etClaveMateria);
        spinnerDocentes = findViewById(R.id.spinnerDocentes);

        cargarDocentes();
    }

    // CARGAR DOCENTES EN EL SPINNER
    private void cargarDocentes() {

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        listaDocentes = new ArrayList<>();
        listaIdsDocentes = new ArrayList<>();

        Cursor fila = bd.rawQuery(
                "SELECT num_empleado, nombre FROM docentes",
                null
        );

        if (fila.moveToFirst()) {

            do {

                // GUARDAR ID
                listaIdsDocentes.add(
                        fila.getInt(0)
                );

                // GUARDAR NOMBRE
                listaDocentes.add(
                        fila.getString(1)
                );

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

    // REGISTRAR MATERIA
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

        // POSICIÓN DEL DOCENTE SELECCIONADO
        int posicion = spinnerDocentes.getSelectedItemPosition();

        // OBTENER ID REAL DEL DOCENTE
        int docenteId = listaIdsDocentes.get(posicion);

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();

        registro.put("clave", Integer.parseInt(clave));
        registro.put("nombre_materia", nombre);

        registro.put("docente_id", docenteId);

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
                    "Error al registrar",
                    Toast.LENGTH_SHORT
            ).show();
        }

        bd.close();
    }
}