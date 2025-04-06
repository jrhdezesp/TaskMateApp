package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActualizarTarea extends AppCompatActivity {

    EditText actTitulo,actDescripcion;
    Button btnActualizarTarea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_tarea);


        btnActualizarTarea = findViewById(R.id.btnActualizarTarea);
        actTitulo = findViewById(R.id.txtTituloActualizar);
        actDescripcion = findViewById(R.id.txtDescripcionActualizar);

        String titulo = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        int id = getIntent().getIntExtra("id",0);
        actTitulo.setText(titulo);
        actDescripcion.setText(desc);


        String sId = String.valueOf(id);

        btnActualizarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MantenimientoTareas mantenimientoTareas = new MantenimientoTareas(ActualizarTarea.this);
                mantenimientoTareas.actualizarDatos(actTitulo.getText().toString(),actDescripcion.getText().toString(),sId);
                Toast.makeText(ActualizarTarea.this,"Los datos han sido actualizados.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActualizarTarea.this,TareasDiarias.class));
            }
        });


    }
}