package com.example.taskmateprueba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TareasDiarias extends AppCompatActivity {

    FloatingActionButton btnAgregar;
    RecyclerView recyclerView;
    ArrayList<TaskModel> arrayList = new ArrayList<>();
    MantenimientoTareas mantenimientoTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas_diarias);
        btnAgregar = findViewById(R.id.btnAgregar);
        recyclerView = findViewById(R.id.recyclerview);
        mantenimientoTareas = new MantenimientoTareas(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TaskAdapter adapter = new TaskAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
        cargarTareas();

        btnAgregar.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               startActivity(new Intent(TareasDiarias.this,AgregarTarea.class));
           }
        });
    }

    private void cargarTareas() {
        arrayList.clear();
        Cursor cursor = mantenimientoTareas.cargarDatos();
        while(cursor.moveToNext()) {
            arrayList.add(new TaskModel(cursor.getString(1), cursor.getString(2), cursor.getInt(0)));
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    protected void onResume() {
        super.onResume();
        cargarTareas();
    }
}