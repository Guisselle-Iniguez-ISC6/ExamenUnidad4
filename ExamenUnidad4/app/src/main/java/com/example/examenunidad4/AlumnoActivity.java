package com.example.examenunidad4;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AlumnoActivity extends AppCompatActivity {

    private TableLayout tabla;
    private TextView tvBienvenida;
    private String nombreAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_alumno);

        tabla = findViewById(R.id.tablaCalificaciones);
        tvBienvenida = findViewById(R.id.tvBienvenidaAlum);

        nombreAlumno = getIntent().getStringExtra("nombre_usuario");
        tvBienvenida.setText("¡Bienvenido, " + nombreAlumno + "!");

        cargarBoleta();
    }

    private void cargarBoleta() {
        AdminSQLite admin = new AdminSQLite(this, "controlEscolar", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        Cursor cursorMaterias = bd.rawQuery("select materias from usuarios where nombre='" + nombreAlumno + "'", null);

        if (cursorMaterias.moveToFirst()) {
            String todasLasMaterias = cursorMaterias.getString(0);
            if (todasLasMaterias != null && !todasLasMaterias.isEmpty()) {
                String[] listaMaterias = todasLasMaterias.split(", ");

                for (String materia : listaMaterias) {
                    Cursor cCal = bd.rawQuery("select nota from calificaciones where alumno='" + nombreAlumno + "' AND materia='" + materia + "'", null);

                    String notaFinal = "0"; // Por defecto 0
                    if (cCal.moveToFirst()) {
                        notaFinal = cCal.getString(0);
                    }
                    cCal.close();

                    agregarFilaTabla(materia, notaFinal);
                }
            }
        }
        cursorMaterias.close();
        bd.close();
    }

    private void agregarFilaTabla(String materia, String nota) {
        TableRow fila = new TableRow(this);
        fila.setPadding(10, 15, 10, 15);

        TextView tvMat = new TextView(this);
        tvMat.setText(materia);
        tvMat.setTextColor(Color.BLACK);

        TextView tvNota = new TextView(this);
        tvNota.setText(nota);
        tvNota.setGravity(Gravity.CENTER);
        try {
            double n = Double.parseDouble(nota);
            tvNota.setTextColor(n >= 70 ? Color.parseColor("#2E7D32") : Color.parseColor("#660000"));
        } catch (Exception e) { tvNota.setTextColor(Color.BLACK); }

        fila.addView(tvMat);
        fila.addView(tvNota);
        tabla.addView(fila);
    }
}