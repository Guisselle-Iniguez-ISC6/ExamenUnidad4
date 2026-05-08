package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

public class Alumnos extends AppCompatActivity {
    // Actualizamos los nombres para que coincidan con el nuevo XML
    private EditText nomCompleto, password;
    private Spinner spinnerTipo;
    private CheckBox cbProg, cbBD, cbIA, cbRedes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_activity);

        // Enlace de componentes
        nomCompleto = findViewById(R.id.nomCompleto);
        password = findViewById(R.id.password);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        cbProg = findViewById(R.id.cbProgramacion);
        cbBD = findViewById(R.id.cbBaseDatos);
        cbIA = findViewById(R.id.cbIA);
        cbRedes = findViewById(R.id.cbRedes);
    }

    public void GuardarAlum(View v) {
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = nomCompleto.getText().toString();
        String pass = password.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        // 1. VALIDACIÓN DE CONTRASEÑA SEGÚN TIPO
        if (tipo.equals("Maestra/Docente")) {
            // 2 letras y 8 números
            if (!pass.matches("^[a-zA-Z]{2}[0-9]{8}$")) {
                Toast.makeText(this, "Maestra: La clave debe tener 2 letras y 8 números", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            // 1 letra y 8 números (Alumno)
            if (!pass.matches("^[a-zA-Z]{1}[0-9]{8}$")) {
                Toast.makeText(this, "Alumno: La clave debe tener 1 letra y 8 números", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // 2. LÓGICA DE MATERIAS (Solo las seleccionadas)
        String materiasSeleccionadas = "";
        if (cbProg.isChecked()) materiasSeleccionadas += "Programación ";
        if (cbBD.isChecked()) materiasSeleccionadas += "Bases de Datos ";
        if (cbIA.isChecked()) materiasSeleccionadas += "IA ";
        if (cbRedes.isChecked()) materiasSeleccionadas += "Redes ";

        if (!nombre.isEmpty() && !pass.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("tipo", tipo);
            registro.put("password", pass);
            registro.put("materias", materiasSeleccionadas.trim());

            // Nota: Asegúrate de que tu tabla en AdminSQLite tenga estas columnas
            bd.insert("usuarios", null, registro);
            bd.close();

            limpiarCampos();
            Toast.makeText(this, "Registro exitoso en la base de datos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método de consulta simplificado para el nuevo diseño
    public void consultaNumControl(View v) {
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        String nombreABuscar = nomCompleto.getText().toString();

        if (!nombreABuscar.isEmpty()) {
            Cursor fila = bd.rawQuery("select tipo, password, materias from usuarios where nombre='" + nombreABuscar + "'", null);

            if (fila.moveToFirst()) {
                password.setText(fila.getString(1));
                // Aquí podrías marcar los checkboxes basándote en fila.getString(2)
                Toast.makeText(this, "Usuario encontrado como: " + fila.getString(0), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_SHORT).show();
            }
            fila.close();
        }
        bd.close();
    }

    private void limpiarCampos() {
        nomCompleto.setText("");
        password.setText("");
        cbProg.setChecked(false);
        cbBD.setChecked(false);
        cbIA.setChecked(false);
        cbRedes.setChecked(false);
    }

    // Implementación rápida de los otros métodos siguiendo tu lógica original...
    public void consultarNom(View v) { consultaNumControl(v); }

    public void eliminaNumControl(View v) {
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nombre = nomCompleto.getText().toString();
        int cant = bd.delete("usuarios", "nombre='" + nombre + "'", null);
        bd.close();
        limpiarCampos();
        if (cant == 1) Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
    }
}