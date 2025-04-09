package com.example.taskmateprueba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Tareas extends AppCompatActivity {

    EditText txtTitulo, txtDescripcion;
    Button btnAgregarTask;
    RadioGroup tipo;
    AdminSQLiteOpen admin;
    private SesionManager sesionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        txtTitulo = findViewById(R.id.txtTareaTitulo);
        txtDescripcion = findViewById(R.id.txtTareaDescripcion);
        btnAgregarTask = findViewById(R.id.btnGuardarTarea);
        tipo = findViewById(R.id.radioGroupPrioridad);
        admin = new AdminSQLiteOpen(Tareas.this);

        // Inicializa el SesionManager
        sesionManager = new SesionManager(this); // Asegúrate de pasar el contexto correctamente

        btnAgregarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifica si el SesionManager ha sido inicializado correctamente
                if (sesionManager != null) {
                    // Obtener el ID del usuario desde la sesión
                    int usuarioId = sesionManager.obtenerUsuarioId();

                    if (txtTitulo.length() > 0 && txtDescripcion.length() > 0 && tipo.getCheckedRadioButtonId() != -1) {
                        int selectedId = tipo.getCheckedRadioButtonId();
                        RadioButton selectedRadioButton = findViewById(selectedId);
                        String tipoTarea = selectedRadioButton.getText().toString();

                        // Insertar la tarea junto con el usuarioId
                        admin.insertarTask(txtTitulo.getText().toString(), txtDescripcion.getText().toString(), tipoTarea, usuarioId);
                        Toast.makeText(Tareas.this, "Los datos se agregaron correctamente", Toast.LENGTH_SHORT).show();
                        txtTitulo.setText("");
                        txtDescripcion.setText("");
                        tipo.clearCheck();
                        finish();
                    } else {
                        if (txtTitulo.length() == 0 || txtDescripcion.length() == 0) {
                            Toast.makeText(Tareas.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Tareas.this, "Seleccione un tipo de tarea", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Tareas.this, "No se pudo obtener la sesión del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
