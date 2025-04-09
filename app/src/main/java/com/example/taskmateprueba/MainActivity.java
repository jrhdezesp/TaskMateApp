package com.example.taskmateprueba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton btnAgregarTask;
    RecyclerView recyclerView;
    ArrayList<ModeloTareas> arrayList = new ArrayList<>();
    AdminSQLiteOpen admin;
    AdaptadorTareas adapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    // 👤 Sesión
    private SesionManager sesionManager;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Sesión
        sesionManager = new SesionManager(this);
        if (!sesionManager.haySesionActiva()) {
            // Redirigir si no hay sesión
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        }

        usuarioId = sesionManager.obtenerUsuarioId();

        // Toolbar + Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageButton menuButton = findViewById(R.id.menu_button);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Opem, R.string.Close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.color1));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Inicializar componentes de tareas
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        admin = new AdminSQLiteOpen(this);
        adapter = new AdaptadorTareas(this, arrayList);
        recyclerView.setAdapter(adapter);

        btnAgregarTask = findViewById(R.id.btnAgregarTask);
        btnAgregarTask.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Tareas.class));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarTareas() {
        arrayList.clear();
        Cursor cursor = admin.cargarTodas(usuarioId);
        while (cursor.moveToNext()) {
            arrayList.add(new ModeloTareas(cursor.getString(1), cursor.getString(2), cursor.getInt(0)));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareas();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;

        if (id == R.id.Home) {
            // Ya estás en MainActivity
        } else if (id == R.id.Diario) {
            intent = new Intent(this, Diario.class);
        } else if (id == R.id.TareasDiarias) {
            intent = new Intent(this, TareasDiarias.class);
        } else if (id == R.id.Calendario) {
            intent = new Intent(this, Calendario.class);
        } else if (id == R.id.Leves) {
            intent = new Intent(this, TareasLeves.class);
        } else if (id == R.id.Moderadas) {
            intent = new Intent(this, TareasModeradas.class);
        } else if (id == R.id.Urgentes) {
            intent = new Intent(this, TareasUrgentes.class);
        } else if (id == R.id.CerrarSesion) {
            sesionManager.cerrarSesion();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
        }

        if (intent != null) startActivity(intent);

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
}
