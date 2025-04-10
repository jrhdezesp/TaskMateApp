package com.example.taskmateprueba;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    ArrayList<TaskModel> arrayList = new ArrayList<>();

    public TaskAdapter(Context context, ArrayList<TaskModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.titulo.setText(arrayList.get(position).getTitle());
        holder.descripcion.setText(arrayList.get(position).getDescription());

        holder.cardView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Borrar Tarea")
                    .setMessage("¿Está seguro de que desea borrar la tarea?")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        AdminSQLiteOpen admin = new AdminSQLiteOpen(context);
                        String id = String.valueOf(arrayList.get(position).getId());
                        admin.eliminarDiaria(id); // ← usamos el método de AdminSQLiteOpen
                        dialogInterface.dismiss();
                        context.startActivity(new Intent(context, TareasDiarias.class));
                    })
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setIcon(R.drawable.alerta)
                    .show();
            return false;
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActualizarTarea.class);
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
                Log.e("TaskAdapter", "Uno de los TextViews es null");
            }
        }
    }
}

