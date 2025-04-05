package com.example.taskmateprueba;

import android.content.Intent;
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

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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


        // Configurar el listener para el botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Login(View view) {
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.Home) {
            Intent inicio = new Intent(this, MainActivity.class);
            startActivity(inicio);
        }else if (menuItem.getItemId() == R.id.Diario) {
            Intent diario = new Intent(this, Diario.class);
            startActivity(diario);

        }else if (menuItem.getItemId() == R.id.TareasDiarias) {
            Intent diarias = new Intent(this, TareasDiarias.class);
            startActivity(diarias);

        }else if (menuItem.getItemId() == R.id.Calendario) {
            Intent calendario = new Intent(this, Calendario.class);
            startActivity(calendario);

        }else if (menuItem.getItemId() == R.id.Leves) {
            Intent leves = new Intent(this, TareasLeves.class);
            startActivity(leves);

        }else if (menuItem.getItemId() == R.id.Moderadas) {
            Intent moderadas = new Intent(this, TareasModeradas.class);
            startActivity(moderadas);

        }else if (menuItem.getItemId() == R.id.Urgentes) {
            Intent urgentes = new Intent(this, TareasUrgentes.class);
            startActivity(urgentes);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}