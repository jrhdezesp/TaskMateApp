package com.example.taskmateprueba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
        recyclerView = findViewById(R.id.recyclerview); // Asegúrate que el ID exista en activity_main.xml
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        admin = new AdminSQLiteOpen(this);
        adapter = new AdaptadorTareas(this, arrayList);
        recyclerView.setAdapter(adapter);

        btnAgregarTask = findViewById(R.id.btnAgregarTask); // Asegúrate que el botón esté en el XML
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
        Cursor cursor = admin.cargarTodas(); // Puedes cambiar a cargarTodas() si deseas incluir todas
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
        if (id == R.id.Home) {
            // Ya estás en MainActivity
        } else if (id == R.id.Diario) {
            startActivity(new Intent(this, Diario.class));
        } else if (id == R.id.TareasDiarias) {
            startActivity(new Intent(this, TareasDiarias.class));
        } else if (id == R.id.Calendario) {
            startActivity(new Intent(this, Calendario.class));
        } else if (id == R.id.Leves) {
            startActivity(new Intent(this, TareasLeves.class));
        } else if (id == R.id.Moderadas) {
            startActivity(new Intent(this, TareasModeradas.class));
        } else if (id == R.id.Urgentes) {
            startActivity(new Intent(this, TareasUrgentes.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Login(View view) {
        Intent login = new Intent(this, Login.class);
        startActivity(login);
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
