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

public class Diario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton btnAgregarNota;
    RecyclerView recyclerView;
    ArrayList<NoteModel> arrayList = new ArrayList<>();
    AdminSQLiteOpen admin; // ← Reemplazamos MantenimientoJournal por AdminSQLiteOpen
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diario);

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

        btnAgregarNota = findViewById(R.id.btnAgregarNota);
        recyclerView = findViewById(R.id.recyclerview);
        admin = new AdminSQLiteOpen(this); // ← Usamos AdminSQLiteOpen
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        cargarNotas();

        btnAgregarNota.setOnClickListener(view ->
                startActivity(new Intent(Diario.this, AgregarNota.class))
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarNotas() {
        arrayList.clear();
        Cursor cursor = admin.cargarNotas(); // ← Cambiado
        while (cursor.moveToNext()) {
            arrayList.add(new NoteModel(cursor.getString(1), cursor.getString(2), cursor.getInt(0)));
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(this, MainActivity.class));
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
        cargarNotas(); // Refresca notas al volver
    }
}
