package com.example.taskmateprueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdministracionUsuarios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administracion_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void crearUsuario(View view) {
        EditText txtUsuario = findViewById(R.id.txtAgregarUsuario);
        EditText txtClave = findViewById(R.id.txtAgregarClave);
        EditText txtCorreo = findViewById(R.id.txtAgregarCorreo);

        String usuario = txtUsuario.getText().toString().trim();
        String clave = txtClave.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();

        if (usuario.isEmpty() || clave.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        } else {
            AdminSQLiteOpen db = new AdminSQLiteOpen(this);
            if (db.usuarioExiste(usuario)) {
                Toast.makeText(this, "El usuario ya existe, elige otro nombre", Toast.LENGTH_SHORT).show();
            } else {
                db.insertarUsuario(usuario, clave, correo);
                Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();

                // Limpiar campos
                txtUsuario.setText("");
                txtClave.setText("");
                txtCorreo.setText("");
            }
        }
    }

    public void Login(View view){
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }
}
