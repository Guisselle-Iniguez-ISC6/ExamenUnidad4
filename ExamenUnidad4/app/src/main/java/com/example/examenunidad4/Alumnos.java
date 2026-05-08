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
    // Componentes del layout
    private EditText nomCompleto, password;
    private Spinner spinnerTipo;
    
    // Lista para manejar CheckBoxes dinámicos de materias
    private List<CheckBox> listaCheckBoxesMaterias = new ArrayList<>();
    
    // Variables para control de visibilidad y contenedor
    private LinearLayout layoutMaterias;
    private TextView tvTituloMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_activity);

        // Enlace de componentes
        nomCompleto = findViewById(R.id.nomCompleto);
        password = findViewById(R.id.password);
        spinnerTipo = findViewById(R.id.spinnerTipo);

        // Contenedores para las materias
        layoutMaterias = findViewById(R.id.layoutMaterias);
        tvTituloMaterias = findViewById(R.id.tvTituloMaterias);

        // CARGAMOS LAS MATERIAS DESDE LA BASE DE DATOS
        cargarMateriasDesdeBD();

        // Lógica para ocultar/mostrar materias según el tipo de usuario
        spinnerTipo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String seleccionado = parent.getItemAtPosition(position).toString();
                
                // Si es Estudiante o Alumno, se muestra la sección de materias
                if (seleccionado.equals("Estudiante") || seleccionado.equals("Alumno")) {
                    tvTituloMaterias.setVisibility(View.VISIBLE);
                    layoutMaterias.setVisibility(View.VISIBLE);
                } else {
                    // Si es Docente, se oculta y se limpian las selecciones
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

        // Limpiamos el layout y la lista antes de cargar
        layoutMaterias.removeAllViews();
        listaCheckBoxesMaterias.clear();

        // Consultamos las materias registradas en la tabla 'materias'
        Cursor fila = bd.rawQuery("SELECT nombre_materia FROM materias", null);

        if (fila.moveToFirst()) {
            do {
                String nombreMateria = fila.getString(0);
                
                // Creamos un CheckBox por cada materia
                CheckBox cb = new CheckBox(this);
                cb.setText(nombreMateria);
                cb.setTextSize(16);
                
                // Lo agregamos al layout y a nuestra lista para poder leerlos después
                layoutMaterias.addView(cb);
                listaCheckBoxesMaterias.add(cb);
                
            } while (fila.moveToNext());
        } else {
            // Mensaje si no hay materias
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

    // MÉTODO PARA GUARDAR NUEVO (EVITA DUPLICADOS)
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

        // VALIDACIÓN DE CONTRASEÑA
        if (!validarFormatoPassword(pass, tipo)) return;

        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("tipo", tipo);
        registro.put("password", pass);
        registro.put("materias", obtenerMateriasSeleccionadas(tipo));

        // Usamos insert para que falle si el nombre (PK) ya existe
        long res = bd.insert("usuarios", null, registro);
        bd.close();

        if (res != -1) {
            limpiarCampos();
            Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: El usuario ya existe. Use 'Modificar' para actualizar.", Toast.LENGTH_LONG).show();
        }
    }

    // MÉTODO PARA MODIFICAR USUARIO (Se habilita tras consultar)
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

        // Actualizamos basándonos en el nombre (llave primaria)
        int cant = bd.update("usuarios", registro, "nombre=?", new String[]{nombre});
        bd.close();

        if (cant == 1) {
            Toast.makeText(this, "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró el usuario para modificar", Toast.LENGTH_SHORT).show();
        }
    }

    // Método helper para validar contraseña
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

    // Método helper para obtener string de materias
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
                
                // Recuperar y marcar las materias seleccionadas
                String matRecuperadas = fila.getString(2);
                limpiarCheckBoxes();
                
                if (matRecuperadas != null && !matRecuperadas.isEmpty()) {
                    for (CheckBox cb : listaCheckBoxesMaterias) {
                        if (matRecuperadas.contains(cb.getText().toString())) {
                            cb.setChecked(true);
                        }
                    }
                }

                // Actualizar spinner y visibilidad
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
