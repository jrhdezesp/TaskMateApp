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

public class Login extends AppCompatActivity {

    EditText usuarioInput, claveInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarioInput = findViewById(R.id.editTextText);
        claveInput = findViewById(R.id.editTextTextPassword);
    }

    public void Menu(View view){
        String usuario = usuarioInput.getText().toString().trim();
        String clave = claveInput.getText().toString().trim();

        AdminSQLiteOpen db = new AdminSQLiteOpen(this);
        int usuarioId = db.verificarUsuario(usuario, clave);

        if (usuarioId != -1) {
            SesionManager sesion = new SesionManager(this);
            sesion.guardarUsuarioId(usuarioId);

            Intent menu = new Intent(this, MainActivity.class);
            startActivity(menu);
            finish();
        } else {
            Toast.makeText(this, "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    public void nuevoUsuario(View view){
        Intent nuevo = new Intent(this, AdministracionUsuarios.class);
        startActivity(nuevo);
    }
}
