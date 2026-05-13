package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Alumnos extends AppCompatActivity {
    private EditText nomCompleto, password;
    private Spinner spinnerTipo;

    private List<CheckBox> listaCheckBoxesMaterias = new ArrayList<>();

    private LinearLayout layoutMaterias;
    private TextView tvTituloMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_activity);

        nomCompleto = findViewById(R.id.nomCompleto);
        password = findViewById(R.id.password);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        layoutMaterias = findViewById(R.id.layoutMaterias);
        tvTituloMaterias = findViewById(R.id.tvTituloMaterias);

        cargarMateriasDesdeBD();

        spinnerTipo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String seleccionado = parent.getItemAtPosition(position).toString();

                if (seleccionado.equals("Estudiante") || seleccionado.equals("Alumno")) {
                    tvTituloMaterias.setVisibility(View.VISIBLE);
                    layoutMaterias.setVisibility(View.VISIBLE);
                } else {
                    tvTituloMaterias.setVisibility(View.GONE);
                    layoutMaterias.setVisibility(View.GONE);
                    limpiarCheckBoxes();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void cargarMateriasDesdeBD() {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();

        layoutMaterias.removeAllViews();
        listaCheckBoxesMaterias.clear();

        Cursor fila = bd.rawQuery("SELECT nombre_materia FROM materias", null);

        if (fila.moveToFirst()) {
            do {
                String nombreMateria = fila.getString(0);

                CheckBox cb = new CheckBox(this);
                cb.setText(nombreMateria);
                cb.setTextSize(16);
                
                layoutMaterias.addView(cb);
                listaCheckBoxesMaterias.add(cb);
                
            } while (fila.moveToNext());
        } else {
            TextView tv = new TextView(this);
            tv.setText("No hay materias dadas de alta.");
            layoutMaterias.addView(tv);
        }
        fila.close();
        bd.close();
    }

    public void irAMaterias(View v) {
        Intent i = new Intent(this, Materias.class);
        startActivity(i);
    }

    public void GuardarAlum(View v) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = nomCompleto.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (nombre.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarFormatoPassword(pass, tipo)) return;

        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("tipo", tipo);
        registro.put("password", pass);
        registro.put("materias", obtenerMateriasSeleccionadas(tipo));

        long res = bd.insert("usuarios", null, registro);
        bd.close();

        if (res != -1) {
            limpiarCampos();
            Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: El usuario ya existe. Use 'Modificar' para actualizar.", Toast.LENGTH_LONG).show();
        }
    }

    public void ModificarAlum(View v) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String nombre = nomCompleto.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (nombre.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Primero consulte a un usuario o llene los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!validarFormatoPassword(pass, tipo)) return;

        ContentValues registro = new ContentValues();
        registro.put("tipo", tipo);
        registro.put("password", pass);
        registro.put("materias", obtenerMateriasSeleccionadas(tipo));

        int cant = bd.update("usuarios", registro, "nombre=?", new String[]{nombre});
        bd.close();

        if (cant == 1) {
            Toast.makeText(this, "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró el usuario para modificar", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarFormatoPassword(String pass, String tipo) {
        if (tipo.equals("Maestra/Docente")) {
            if (!pass.matches("^[a-zA-Z]{2}[0-9]{8}$")) {
                Toast.makeText(this, "Maestra: 2 letras y 8 números", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            if (!pass.matches("^[a-zA-Z]{1}[0-9]{8}$")) {
                Toast.makeText(this, "Alumno: 1 letra y 8 números", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private String obtenerMateriasSeleccionadas(String tipo) {
        StringBuilder materias = new StringBuilder();
        if (tipo.equals("Estudiante") || tipo.equals("Alumno")) {
            for (CheckBox cb : listaCheckBoxesMaterias) {
                if (cb.isChecked()) {
                    materias.append(cb.getText().toString()).append(", ");
                }
            }
        }
        String matFinal = materias.toString();
        if (matFinal.endsWith(", ")) {
            matFinal = matFinal.substring(0, matFinal.length() - 2);
        }
        return matFinal;
    }

    public void consultaNumControl(View v) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getReadableDatabase();
        String nombreABuscar = nomCompleto.getText().toString().trim();

        if (!nombreABuscar.isEmpty()) {
            Cursor fila = bd.rawQuery("SELECT tipo, password, materias FROM usuarios WHERE nombre=?", new String[]{nombreABuscar});

            if (fila.moveToFirst()) {
                String tipo = fila.getString(0);
                password.setText(fila.getString(1));

                String matRecuperadas = fila.getString(2);
                limpiarCheckBoxes();
                
                if (matRecuperadas != null && !matRecuperadas.isEmpty()) {
                    for (CheckBox cb : listaCheckBoxesMaterias) {
                        if (matRecuperadas.contains(cb.getText().toString())) {
                            cb.setChecked(true);
                        }
                    }
                }

                if (tipo.equals("Alumno") || tipo.equals("Estudiante")) {
                    spinnerTipo.setSelection(0);
                    tvTituloMaterias.setVisibility(View.VISIBLE);
                    layoutMaterias.setVisibility(View.VISIBLE);
                } else {
                    spinnerTipo.setSelection(1);
                    tvTituloMaterias.setVisibility(View.GONE);
                    layoutMaterias.setVisibility(View.GONE);
                }

                Toast.makeText(this, "Usuario encontrado. Ahora puede modificarlo.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_SHORT).show();
            }
            fila.close();
        } else {
            Toast.makeText(this, "Ingresa un nombre para consultar", Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }

    private void limpiarCampos() {
        nomCompleto.setText("");
        password.setText("");
        limpiarCheckBoxes();
    }

    private void limpiarCheckBoxes() {
        for (CheckBox cb : listaCheckBoxesMaterias) {
            cb.setChecked(false);
        }
    }

    public void consultarNom(View v) { 
        consultaNumControl(v); 
    }

    public void eliminaNumControl(View v) {
        AdminSQLite admin = new AdminSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nombre = nomCompleto.getText().toString().trim();
        
        if (!nombre.isEmpty()) {
            int cant = bd.delete("usuarios", "nombre=?", new String[]{nombre});
            bd.close();
            limpiarCampos();
            if (cant == 1) {
                Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingresa el nombre para eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}
