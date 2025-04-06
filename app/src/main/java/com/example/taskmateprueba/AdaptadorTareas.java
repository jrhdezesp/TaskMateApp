package com.example.taskmateprueba;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorTareas extends RecyclerView.Adapter<AdaptadorTareas.ViewHolder> {

    Context context;
    ArrayList<ModeloTareas> arrayList = new ArrayList<>();

    public AdaptadorTareas(Context context, ArrayList<ModeloTareas> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AdaptadorTareas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTareas.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.titulo.setText(arrayList.get(position).getTitle());
        holder.descripcion.setText(arrayList.get(position).getDescription());

        holder.cardView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Borrar Tarea")
                    .setMessage("¿Está seguro de que desea borrar la tarea?")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        AdminSQLiteOpen admin = new AdminSQLiteOpen(context);
                        String id = String.valueOf(arrayList.get(position).getId());
                        admin.eliminarTask(id);
                        dialogInterface.dismiss();

                        if (context instanceof AppCompatActivity) {
                            ((AppCompatActivity) context).recreate();
                        }
                    })
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setIcon(R.drawable.alerta)
                    .show();
            return false;
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActualizarTask.class);
            intent.putExtra("titulo", arrayList.get(position).getTitle());
            intent.putExtra("descripcion", arrayList.get(position).getDescription());
            intent.putExtra("id", arrayList.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, descripcion;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.lblTaskTitulo);
            descripcion = itemView.findViewById(R.id.lblTaskDescripcion);
            cardView = itemView.findViewById(R.id.cardView);

            if (titulo == null || descripcion == null) {
                Log.e("AdaptadorTareas", "Uno de los TextViews es null");
            }
        }
    }
}

