package com.example.examenunidad4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AdminSQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "controlEscolar";
    private static final int DATABASE_VERSION = 1;

    // Constructor simplificado
    public AdminSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public AdminSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios(" +
                "nombre TEXT PRIMARY KEY, " +
                "tipo TEXT, " +
                "password TEXT, " +
                "materias TEXT)");

        db.execSQL("CREATE TABLE materias(" +
                "clave TEXT PRIMARY KEY, " +
                "nombre_materia TEXT, " +
                "docente_asignado TEXT)");

        db.execSQL("CREATE TABLE calificaciones(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "alumno TEXT, " +
                "materia TEXT, " +
                "nota TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS materias");
        db.execSQL("DROP TABLE IF EXISTS calificaciones");
        onCreate(db);
    }
}