package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Tareas extends AppCompatActivity {

    EditText txtTitulo,txtDescripcion;
    Button btnAgregarTask;

    AdminSQLiteOpen admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas);

        txtTitulo = findViewById(R.id.txtTareaTitulo);
        txtDescripcion = findViewById(R.id.txtTareaDescripcion);
        btnAgregarTask = findViewById(R.id.btnGuardarTarea);
        admin = new AdminSQLiteOpen(Tareas.this);


        btnAgregarTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txtTitulo.length()>0 && txtDescripcion.length()>0){
                    admin.insertarTask(txtTitulo.getText().toString(),txtDescripcion.getText().toString());
                    Toast.makeText(Tareas.this,"Los datos se agregaron correctamente", Toast.LENGTH_SHORT).show();
                    txtTitulo.setText("");
                    txtDescripcion.setText("");
                }else{
                    Toast.makeText(Tareas.this,"Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}