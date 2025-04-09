package com.example.taskmateprueba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class TareasLeves extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton btnAgregarTask;
    RecyclerView recyclerView;
    ArrayList<ModeloTareas> arrayList = new ArrayList<>();
    AdminSQLiteOpen admin;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    SesionManager sesionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas_leves);

        // Inicializar el DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Configurar el Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Opem, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        btnAgregarTask = findViewById(R.id.btnAgregarTask);
        recyclerView = findViewById(R.id.recyclerview);
        admin = new AdminSQLiteOpen(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorTareas adapter = new AdaptadorTareas(this, arrayList);
        recyclerView.setAdapter(adapter);
        cargarTareas();

        btnAgregarTask.setOnClickListener(view ->
                startActivity(new Intent(TareasLeves.this, Tareas.class))
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarTareas() {
        arrayList.clear();
        int usuarioId = sesionManager.obtenerUsuarioId();
        Cursor cursor = admin.cargarLeve(usuarioId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new ModeloTareas(cursor.getString(1), cursor.getString(2), cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.Diario) {
            startActivity(new Intent(this, Diario.class));
        } else if (id == R.id.Calendario) {
            startActivity(new Intent(this, Calendario.class));
        } else if (id == R.id.Leves) {
        } else if (id == R.id.Moderadas) {
            startActivity(new Intent(this, TareasModeradas.class));
        } else if (id == R.id.Urgentes) {
            startActivity(new Intent(this, TareasUrgentes.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareas();
    }
}
