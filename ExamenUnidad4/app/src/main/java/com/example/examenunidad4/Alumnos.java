package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Alumnos extends AppCompatActivity {

    private EditText nomCompleto, numControl, carrera;

    private CheckBox cbProg, cbBD, cbIA, cbRedes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_activity);

        nomCompleto = findViewById(R.id.nomCompleto);
        numControl = findViewById(R.id.password); // usa tu id actual
        carrera = findViewById(R.id.carrera);

        cbProg = findViewById(R.id.cbProgramacion);
        cbBD = findViewById(R.id.cbBaseDatos);
        cbIA = findViewById(R.id.cbIA);
        cbRedes = findViewById(R.id.cbRedes);
    }

    // GUARDAR ALUMNO
    public void GuardarAlum(View v) {

        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = nomCompleto.getText().toString().trim();
        String control = numControl.getText().toString().trim();
        String carreraTexto = carrera.getText().toString().trim();

        if (nombre.isEmpty()
                || control.isEmpty()
                || carreraTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        ContentValues registro = new ContentValues();

        registro.put(
                "numcontrol",
                Integer.parseInt(control)
        );

        registro.put(
                "nombre",
                nombre
        );

        registro.put(
                "carrera",
                carreraTexto
        );

        long resultado = bd.insert(
                "alumnos",
                null,
                registro
        );

        // MATERIAS SELECCIONADAS
        registrarMateriaAlumno(bd, control);

        if (resultado != -1) {

            Toast.makeText(
                    this,
                    "Alumno registrado",
                    Toast.LENGTH_SHORT
            ).show();

            limpiarCampos();

        } else {

            Toast.makeText(
                    this,
                    "Error al registrar",
                    Toast.LENGTH_SHORT
            ).show();
        }

        bd.close();
    }

    // REGISTRAR MATERIAS DEL ALUMNO
    private void registrarMateriaAlumno(SQLiteDatabase bd, String control) {

        int alumnoId = Integer.parseInt(control);

        registrarSiExiste(bd, alumnoId, cbProg, 1);
        registrarSiExiste(bd, alumnoId, cbBD, 2);
        registrarSiExiste(bd, alumnoId, cbIA, 3);
        registrarSiExiste(bd, alumnoId, cbRedes, 4);
    }

    private void registrarSiExiste(SQLiteDatabase bd,
                                   int alumnoId,
                                   CheckBox check,
                                   int materiaId) {

        if (check.isChecked()) {

            ContentValues valores = new ContentValues();

            valores.put("alumno_id", alumnoId);
            valores.put("materia_id", materiaId);
            valores.put("calificacion", 0);

            bd.insert(
                    "calificaciones",
                    null,
                    valores
            );
        }
    }

    // CONSULTAR
    public void consultaNumControl(View v) {

        AdminSQLite admin = new AdminSQLite(this);

        SQLiteDatabase bd = admin.getReadableDatabase();

        String control = numControl.getText().toString();

        Cursor fila = bd.rawQuery(
                "SELECT nombre, carrera " +
                        "FROM alumnos " +
                        "WHERE numcontrol=" + control,
                null
        );

        if (fila.moveToFirst()) {

            nomCompleto.setText(
                    fila.getString(0)
            );

            carrera.setText(
                    fila.getString(1)
            );

            Toast.makeText(
                    this,
                    "Alumno encontrado",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "No existe",
                    Toast.LENGTH_SHORT
            ).show();
        }

        fila.close();
        bd.close();
    }

    // ELIMINAR
    public void eliminaNumControl(View v) {

        AdminSQLite admin = new AdminSQLite(this);

        SQLiteDatabase bd = admin.getWritableDatabase();

        String control = numControl.getText().toString();

        int cantidad = bd.delete(
                "alumnos",
                "numcontrol=" + control,
                null
        );

        bd.delete(
                "calificaciones",
                "alumno_id=" + control,
                null
        );

        bd.close();

        limpiarCampos();

        if (cantidad == 1) {

            Toast.makeText(
                    this,
                    "Alumno eliminado",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Alumno no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public void consultarNom(View v) {
        consultaNumControl(v);
    }

    // LIMPIAR
    private void limpiarCampos() {

        nomCompleto.setText("");
        numControl.setText("");
        carrera.setText("");

        cbProg.setChecked(false);
        cbBD.setChecked(false);
        cbIA.setChecked(false);
        cbRedes.setChecked(false);
    }
}