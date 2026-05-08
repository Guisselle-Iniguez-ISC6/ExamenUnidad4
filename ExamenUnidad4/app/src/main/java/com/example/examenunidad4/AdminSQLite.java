package com.example.examenunidad4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLite extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "escuela.db";

    // Versión
    private static final int DATABASE_VERSION = 1;

    public AdminSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE alumnos (" + "numcontrol INTEGER PRIMARY KEY, " + "nombre TEXT, " + "carrera TEXT)"
        );

        db.execSQL(
                "CREATE TABLE docentes (" + "num_empleado INTEGER PRIMARY KEY, " + "nombre TEXT, " + "direccion TEXT)"
        );

        db.execSQL(
                "CREATE TABLE materias (" + "clave INTEGER PRIMARY KEY, " + "nombre_materia TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alumnos");
        db.execSQL("DROP TABLE IF EXISTS docentes");
        db.execSQL("DROP TABLE IF EXISTS materias");

        onCreate(db);
    }
}