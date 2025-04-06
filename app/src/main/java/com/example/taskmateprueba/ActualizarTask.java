package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActualizarTask extends AppCompatActivity {

    EditText actTitulo,actDescripcion;
    Button btnActualizarTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_task);


        btnActualizarTask = findViewById(R.id.btnActualizarTask);
        actTitulo = findViewById(R.id.txtTituloActualizarTask);
        actDescripcion = findViewById(R.id.txtDescripcionActualizarTask);

        String titulo = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        int id = getIntent().getIntExtra("id",0);
        actTitulo.setText(titulo);
        actDescripcion.setText(desc);


        String sId = String.valueOf(id);

        btnActualizarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdminSQLiteOpen admin = new AdminSQLiteOpen(ActualizarTask.this);
                admin.actualizarTask(actTitulo.getText().toString(),actDescripcion.getText().toString(),sId);
                Toast.makeText(ActualizarTask.this,"Los datos han sido actualizados.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActualizarTask.this,TareasLeves.class));
            }
        });


    }
}