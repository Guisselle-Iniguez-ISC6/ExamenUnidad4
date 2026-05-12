package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class DocenteActivity extends AppCompatActivity {

    private ListView lvMaterias;
    private TextView tvBienvenida;
    private ArrayList<String> listaMaterias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_docente);

        lvMaterias = findViewById(R.id.lvMateriasDocente);
        tvBienvenida = findViewById(R.id.tvBienvenidaDocente);

        // Recibimos el nombre del docente que se logueó
        String nombreDocente = getIntent().getStringExtra("nombre_usuario");
        tvBienvenida.setText("Bienvenido, Prof. " + nombreDocente);

        consultarMateriasAsignadas(nombreDocente);

        // Listener para calificar materia al hacer clic
        lvMaterias.setOnItemClickListener((parent, view, position, id) -> {
            String itemValue = (String) lvMaterias.getItemAtPosition(position);
            
            // Validamos que no sea el mensaje de "no hay materias"
            if (itemValue.contains("Nombre: ")) {
                // Extraemos solo el nombre de la materia (después de "Nombre: ")
                String nombreMat = itemValue.split("Nombre: ")[1];

                Intent intent = new Intent(DocenteActivity.this, CalificarActivity.class);
                intent.putExtra("materia_nombre", nombreMat);
                startActivity(intent);
            }
        });
    }

    private void consultarMateriasAsignadas(String docente) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        // Buscamos en la tabla materias donde el docente sea el que hizo login
        // Nota: Asegúrate de que la columna se llame 'docente_asignado' o 'docente_id' en tu DB
        Cursor fila = bd.rawQuery("select clave, nombre_materia from materias where docente_asignado='" + docente + "'", null);

        if (fila.moveToFirst()) {
            do {
                // Formateamos como se verá en la lista
                String infoMateria = "Clave: " + fila.getString(0) + "\nNombre: " + fila.getString(1);
                listaMaterias.add(infoMateria);
            } while (fila.moveToNext());
        } else {
            listaMaterias.add("No tienes materias asignadas todavía.");
        }
        bd.close();

        // Llenamos el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaMaterias);
        lvMaterias.setAdapter(adapter);
    }
}
