package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActualizarTask extends AppCompatActivity {

    EditText actTitulo, actDescripcion;
    Button btnActualizarTask;
    RadioGroup actTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_task);

        btnActualizarTask = findViewById(R.id.btnActualizarTask);
        actTitulo = findViewById(R.id.txtTituloActualizarTask);
        actDescripcion = findViewById(R.id.txtDescripcionActualizarTask);
        actTipo = findViewById(R.id.radioGroupPrioridad);

        String titulo = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        int id = getIntent().getIntExtra("id", 0);
        actTitulo.setText(titulo);
        actDescripcion.setText(desc);

        btnActualizarTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del RadioButton seleccionado
                int selectedId = actTipo.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String tipoTarea = selectedRadioButton.getText().toString(); // Obtener el texto del RadioButton seleccionado

                AdminSQLiteOpen admin = new AdminSQLiteOpen(ActualizarTask.this);
                admin.actualizarTask(actTitulo.getText().toString(), actDescripcion.getText().toString(), tipoTarea, String.valueOf(id));
                Toast.makeText(ActualizarTask.this, "Los datos han sido actualizados.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}