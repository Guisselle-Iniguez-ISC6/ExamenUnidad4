package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class CalificarActivity extends AppCompatActivity {

    private LinearLayout contenedorLista;
    private String materiaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        materiaSeleccionada = getIntent().getStringExtra("materia_nombre");
        TextView titulo = findViewById(R.id.tvTituloMateria);
        titulo.setText("Calificar: " + materiaSeleccionada);

        contenedorLista = findViewById(R.id.contenedorListaAlumnos);
        cargarAlumnosDeMateria();
    }

    private void cargarAlumnosDeMateria() {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        // Buscamos alumnos que tengan esta materia en su texto de materias
        Cursor fila = bd.rawQuery("select nombre from usuarios where tipo='Alumno' AND materias LIKE '%" + materiaSeleccionada + "%'", null);

        if (fila.moveToFirst()) {
            do {
                String nombreAlum = fila.getString(0);
                agregarFilaAlumno(nombreAlum);
            } while (fila.moveToNext());
        } else {
            Toast.makeText(this, "No hay alumnos inscritos", Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }

    private void agregarFilaAlumno(String nombre) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_alumno_calificar, contenedorLista, false);

        TextView tvNombre = view.findViewById(R.id.tvNombreAlumnoItem);
        EditText etCal = view.findViewById(R.id.etCalificacionItem);
        Button btn = view.findViewById(R.id.btnSubirCal);

        tvNombre.setText(nombre);

        btn.setOnClickListener(v -> {
            String calificacion = etCal.getText().toString().trim();
            if(!calificacion.isEmpty()){
                guardarCalificacionBDD(nombre, calificacion);
            } else {
                Toast.makeText(this, "Ingresa una calificación", Toast.LENGTH_SHORT).show();
            }
        });

        contenedorLista.addView(view);
    }

    private void guardarCalificacionBDD(String alumno, String nota) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("alumno", alumno);
        reg.put("materia", materiaSeleccionada);
        reg.put("nota", nota);
        
        long result = bd.insert("calificaciones", null, reg);
        bd.close();
        
        if (result != -1) {
            Toast.makeText(this, "Calificación de " + alumno + " guardada: " + nota, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar calificación", Toast.LENGTH_SHORT).show();
        }
    }

    public void finalizarCalificacion(View v) {
        finish();
    }
}