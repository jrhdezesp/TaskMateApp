package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AgregarTarea extends AppCompatActivity {

    EditText txtTitulo,txtDescripcion;
    Button btnAgregar;

    AdminSQLiteOpen admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_tarea);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnAgregar = findViewById(R.id.btnAgregarTarea);
        admin = new AdminSQLiteOpen(AgregarTarea.this);


        btnAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txtTitulo.length()>0 && txtDescripcion.length()>0){
                    admin.insertarDiaria(txtTitulo.getText().toString(),txtDescripcion.getText().toString());
                    Toast.makeText(AgregarTarea.this,"Los datos se agregaron correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgregarTarea.this,TareasDiarias.class));
                    txtTitulo.setText("");
                    txtDescripcion.setText("");
                    finish();
                }else{
                    Toast.makeText(AgregarTarea.this,"Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}