package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActualizarNota extends AppCompatActivity {

    EditText actTitulo,actDescripcion;
    Button btnActualizarNota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_nota);


        btnActualizarNota = findViewById(R.id.btnActualizarNota);
        actTitulo = findViewById(R.id.txtTituloActualizar);
        actDescripcion = findViewById(R.id.txtDescripcionActualizar);

        String titulo = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        int id = getIntent().getIntExtra("id",0);
        actTitulo.setText(titulo);
        actDescripcion.setText(desc);

        String sId = String.valueOf(id);

        btnActualizarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdminSQLiteOpen admin = new AdminSQLiteOpen(ActualizarNota.this);
                admin.actualizarNota(actTitulo.getText().toString(),actDescripcion.getText().toString(),sId);
                Toast.makeText(ActualizarNota.this,"Los datos han sido actualizados.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActualizarNota.this,Diario.class));
            }
        });


    }
}