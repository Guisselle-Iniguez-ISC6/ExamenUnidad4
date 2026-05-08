package com.example.examenunidad4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLite extends SQLiteOpenHelper {

    public AdminSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table estudiantes(numcontrol int primary key, nombrealum text, telalum int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de usuarios (alumnos/docentes)
        db.execSQL("create table usuarios(nombre text primary key, tipo text, password text, materias text)");

        // Tabla de materias
        db.execSQL("create table materias(clave text primary key, nombre_materia text, docente_asignado text)");
    }
}
