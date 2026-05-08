package com.example.examenunidad4.;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Alumnos extends AppCompatActivity {
    private EditText numCon,nomAlum,telAlum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numCon=findViewById(R.id.numCon);
        nomAlum=findViewById(R.id.nomAlum);
        telAlum=findViewById(R.id.telAlum);
    }
    public void GuardarAlum(View v) {
        //en caso de no existir la BDD se crea y la llama controlEscolar
        //version:1 es que solo existe la version 1 si hacemos cambios seria un 2
        com.example.proyectobdd.AdminSqLite admin = new com.example.proyectobdd.AdminSqLite(this, "controlEscolar", null, 1);
        //aquí se abre la BDD en modo lectura/escritura
        SQLiteDatabase bd = admin.getWritableDatabase();

        String control = numCon.getText().toString();
        String name = nomAlum.getText().toString();
        String phone = telAlum.getText().toString();
        //Está línea es la que ayuda a crear una nueva fila en la BDD
        ContentValues registro = new ContentValues();
        //put inicializa los datos a guardar
        registro.put("numcontrol", control);
        registro.put("nombrealum", name);
        registro.put("telalum", phone);
        //insertamos los datos en la BDD
        bd.insert("estudiantes", null, registro);
        //no olvides cerrar la BDD
        bd.close();
        numCon.setText("");
        nomAlum.setText("");
        telAlum.setText("");
        //Msj de aviso al usuario
        Toast.makeText(this, "El estudiante se guardo correctamente", Toast.LENGTH_SHORT).show();
    }

    public void consultaNumControl(View v) {
        com.example.proyectobdd.AdminSqLite admin = new com.example.proyectobdd.AdminSqLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String con = numCon.getText().toString();
        // Cursor nos ayuda a almacenar una fila de datos en caso de encontrar
        //a quien buscamos,
        Cursor fila = bd.rawQuery("select nombrealum, telalum from estudiantes where numcontrol=" + con, null);
        //moveToFirst retorna 1 en caso de encontrar el registro 0 en caso contrario
        if (fila.moveToFirst()) {
            //getString obtiene los datos de la tabla
            nomAlum.setText(fila.getString(0));
            telAlum.setText(fila.getString(1));
        } else
            Toast.makeText(this, "Este número de control no existe",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }


    public void consultarNom(View v) {
        com.example.proyectobdd.AdminSqLite admin = new com.example.proyectobdd.AdminSqLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        // Obtenemos el texto del campo del NOMBRE
        String nombre = nomAlum.getText().toString();

        if (!nombre.isEmpty()) {
            // Buscamos por nombrealum. Importante: el nombre va entre comillas simples ''
            // Seleccionamos numcontrol y telalum para rellenar los otros campos
            Cursor fila = bd.rawQuery("select numcontrol, telalum from estudiantes where nombrealum='" + nombre + "'", null);

            if (fila.moveToFirst()) {
                // Llenamos los campos con los datos encontrados
                numCon.setText(fila.getString(0));
                telAlum.setText(fila.getString(1));
            } else {
                Toast.makeText(this, "Este nombre no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Introduce un nombre para buscar", Toast.LENGTH_SHORT).show();
        }
        bd.close();
    }

    public void eliminaNumControl(View v) {
        com.example.proyectobdd.AdminSqLite admin = new com.example.proyectobdd.AdminSqLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String con= numCon.getText().toString();
        //delete retorna un entero, el número de registros que eliminó
        int bandera = bd.delete("estudiantes", "numcontrol=" + con, null);
        bd.close();
        numCon.setText("");
        nomAlum.setText("");
        telAlum.setText("");
        if (bandera == 1)
            Toast.makeText(this, "Estudiante dado de baja",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe el estudiante en la BDD",
                    Toast.LENGTH_SHORT).show();
    }

    public void modificaAlumno(View v) {
        com.example.proyectobdd.AdminSqLite admin = new com.example.proyectobdd.AdminSqLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String control = numCon.getText().toString();
        String nombre = nomAlum.getText().toString();
        String tel = telAlum.getText().toString();
        ContentValues registro = new ContentValues();
        //los put son los campos que modificaremos
        registro.put("numcontrol", control);
        registro.put("nombrealum", nombre);
        registro.put("telalum", tel);
        int cant = bd.update("estudiantes", registro, "numcontrol=" + control, null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast.makeText(this, "No existe el estudiante en la BDD",
                    Toast.LENGTH_SHORT).show();
    }

}