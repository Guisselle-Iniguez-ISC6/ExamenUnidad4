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
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        listaDocentes = new ArrayList<>();

        // Buscamos solo a los que registraste como Docentes
        Cursor fila = bd.rawQuery("select nombre from usuarios where tipo='Maestra/Docente' OR tipo='Docente'", null);

        if (fila.moveToFirst()) {
            do {
                listaDocentes.add(fila.getString(0));
            } while (fila.moveToNext());
        } else {
            listaDocentes.add("No hay docentes registrados");
        }
        bd.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDocentes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDocentes.setAdapter(adapter);
    }

    public void registrarMateria(View v) {
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = etNombreMat.getText().toString();
        String clave = etClaveMat.getText().toString();
        String docente = spinnerDocentes.getSelectedItem().toString();

        if (!nombre.isEmpty() && !clave.isEmpty() && !docente.equals("No hay docentes registrados")) {
            ContentValues registro = new ContentValues();
            registro.put("clave", clave);
            registro.put("nombre_materia", nombre);
            registro.put("docente_asignado", docente);

            // Asegúrate de tener esta tabla 'materias' en tu AdminSQLite
            bd.insert("materias", null, registro);
            bd.close();

            etNombreMat.setText("");
            etClaveMat.setText("");
            Toast.makeText(this, "Materia registrada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Faltan datos o no hay docentes", Toast.LENGTH_SHORT).show();
        }
    }
}