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

        Cursor fila = bd.rawQuery(
                "select nombre from usuarios where tipo='Alumno' AND materias LIKE '%"
                        + materiaSeleccionada + "%'",
                null
        );

        if (fila.moveToFirst()) {

            do {

                String nombreAlum = fila.getString(0);
                agregarFilaAlumno(nombreAlum);

            } while (fila.moveToNext());

        } else {

            Toast.makeText(this,
                    "No hay alumnos inscritos",
                    Toast.LENGTH_SHORT).show();
        }

        fila.close();
        bd.close();
    }

    private void agregarFilaAlumno(String nombre) {

        View view = LayoutInflater.from(this)
                .inflate(R.layout.item_alumno_calificar,
                        contenedorLista,
                        false);

        TextView tvNombre = view.findViewById(R.id.tvNombreAlumnoItem);
        EditText etCal = view.findViewById(R.id.etCalificacionItem);
        Button btn = view.findViewById(R.id.btnSubirCal);

        tvNombre.setText(nombre);

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        Cursor fila = bd.rawQuery(
                "select nota from calificaciones where alumno='"
                        + nombre + "' AND materia='" + materiaSeleccionada + "'",
                null
        );

        if (fila.moveToFirst()) {

            etCal.setText(fila.getString(0));
        }

        fila.close();
        bd.close();

        btn.setOnClickListener(v -> {

            String calificacion = etCal.getText().toString().trim();

            if (calificacion.isEmpty()) {

                Toast.makeText(this,
                        "Ingresa una calificación",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            int nota = Integer.parseInt(calificacion);

            if (nota < 0 || nota > 100) {

                Toast.makeText(this,
                        "La calificación debe estar entre 0 y 100",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            guardarCalificacionBDD(nombre, String.valueOf(nota));
        });

        contenedorLista.addView(view);
    }

    private void guardarCalificacionBDD(String alumno, String nota) {

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select * from calificaciones where alumno='"
                        + alumno + "' AND materia='" + materiaSeleccionada + "'",
                null
        );

        ContentValues reg = new ContentValues();
        reg.put("nota", nota);

        int resultado;

        if (fila.moveToFirst()) {

            resultado = bd.update(
                    "calificaciones",
                    reg,
                    "alumno=? AND materia=?",
                    new String[]{alumno, materiaSeleccionada}
            );

        } else {

            reg.put("alumno", alumno);
            reg.put("materia", materiaSeleccionada);

            long insertado = bd.insert(
                    "calificaciones",
                    null,
                    reg
            );

            resultado = (insertado != -1) ? 1 : 0;
        }

        fila.close();
        bd.close();

        if (resultado > 0) {

            Toast.makeText(this,
                    "Calificación guardada correctamente",
                    Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this,
                    "Error al guardar",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void finalizarCalificacion(View v) {

        finish();
    }
}