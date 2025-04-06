package com.example.taskmateprueba;

public class TaskModel {

    String titulo,descripcion;
    int id;

    public TaskModel(String titulo, String descripcion, int id) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id = id;
    }

    public String getTitle() {
        return titulo;
    }

    public void setTitle(String title) {
        this.titulo = title;
    }

    public String getDescription() {
        return descripcion;
    }

    public void setDescription(String description) {
        this.descripcion = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
