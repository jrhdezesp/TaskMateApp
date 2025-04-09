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

    EditText txtTitulo, txtDescripcion;
    Button btnAgregar;

    AdminSQLiteOpen admin;

    private SesionManager sesionManager;  // Asegúrate de que esta línea esté presente para la declaración de SesionManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_tarea);

        // Inicializar los elementos de la vista
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnAgregar = findViewById(R.id.btnAgregarTarea);

        // Inicializar la instancia de AdminSQLiteOpen
        admin = new AdminSQLiteOpen(AgregarTarea.this);

        // Inicializar el SesionManager
        sesionManager = new SesionManager(this);  // Asegúrate de inicializar el SesionManager

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Verificar que el SesionManager no sea nulo
                if (sesionManager != null) {
                    // Obtener el ID del usuario desde la sesión
                    int usuarioId = sesionManager.obtenerUsuarioId();

                    if (txtTitulo.length() > 0 && txtDescripcion.length() > 0) {
                        // Insertar la tarea diaria junto con el id del usuario
                        admin.insertarDiaria(txtTitulo.getText().toString(), txtDescripcion.getText().toString(), usuarioId);
                        Toast.makeText(AgregarTarea.this, "Los datos se agregaron correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AgregarTarea.this, TareasDiarias.class));
                        txtTitulo.setText("");
                        txtDescripcion.setText("");
                        finish();
                    } else {
                        Toast.makeText(AgregarTarea.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si el SesionManager es nulo, muestra un mensaje de error
                    Toast.makeText(AgregarTarea.this, "Error: Sesion no iniciada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
