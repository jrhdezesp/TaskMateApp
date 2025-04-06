package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AgregarNota extends AppCompatActivity {

    EditText txtTitulo,txtDescripcion;
    Button btnAgregar;

    AdminSQLiteOpen admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_nota);

        txtTitulo = findViewById(R.id.txtTituloNota);
        txtDescripcion = findViewById(R.id.txtDescripcionNota);
        btnAgregar = findViewById(R.id.btnAgregarNota);
        admin = new AdminSQLiteOpen(AgregarNota.this);


        btnAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txtTitulo.length()>0 && txtDescripcion.length()>0){
                    admin.insertarNota(txtTitulo.getText().toString(),txtDescripcion.getText().toString());
                    Toast.makeText(AgregarNota.this,"Los datos se agregaron correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgregarNota.this,Diario.class));
                    txtTitulo.setText("");
                    txtDescripcion.setText("");
                    finish();
                }else{
                    Toast.makeText(AgregarNota.this,"Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}