package com.example.practicabd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSqLite extends SQLiteOpenHelper {

    public AdminSqLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table estudiantes(numcontrol int primary key, nombrealum text, telalum text)");
        db.execSQL("create table docentes(num_empleado int primary key, nombre text, direccion text)");
        db.execSQL("create table materias(clave_ int primary key, nombre_materia text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists estudiantes");
        db.execSQL("drop table if exists docentes");
        db.execSQL("drop table if exists materias");
        onCreate(db);
    }
}