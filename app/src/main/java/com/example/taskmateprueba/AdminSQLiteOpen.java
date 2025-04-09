package com.example.taskmateprueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpen extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskMate.db";
    private static final int VERSION = 2;

    public AdminSQLiteOpen(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, clave TEXT, correo TEXT)");
        db.execSQL("CREATE TABLE journal(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, usuario_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");
        db.execSQL("CREATE TABLE diarias(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, usuario_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");
        db.execSQL("CREATE TABLE tareas(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, tipo TEXT, usuario_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS journal");
        db.execSQL("DROP TABLE IF EXISTS diarias");
        db.execSQL("DROP TABLE IF EXISTS tareas");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    public boolean usuarioExiste(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE usuario = ?", new String[]{usuario});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    // Verificar login
    public int verificarUsuario(String usuario, String clave) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE usuario = ? AND clave = ?", new String[]{usuario, clave});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            return -1;
        }
    }

    // Usuarios
    public void insertarUsuario(String usuario, String clave, String correo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("clave", clave);
        values.put("correo", correo);
        db.insert("usuarios", null, values);
    }

    // Journal
    public void insertarNota(String titulo, String descripcion, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("usuario_id", usuarioId);
        db.insert("journal", null, values);
    }

    public Cursor cargarNotas(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM journal WHERE usuario_id = ?", new String[]{String.valueOf(usuarioId)});
    }

    public void eliminarNota(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("journal", "id=?", new String[]{id});
    }

    public void actualizarNota(String titulo, String descripcion, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.update("journal", values, "id=?", new String[]{id});
    }

    // Diarias
    public void insertarDiaria(String titulo, String descripcion, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("usuario_id", usuarioId);
        db.insert("diarias", null, values);
    }

    public Cursor cargarDiarias(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM diarias WHERE usuario_id = ?", new String[]{String.valueOf(usuarioId)});
    }

    public void eliminarDiaria(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("diarias", "id=?", new String[]{id});
    }

    public void actualizarDiaria(String titulo, String descripcion, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.update("diarias", values, "id=?", new String[]{id});
    }

    // Tareas
    public void insertarTask(String titulo, String descripcion, String tipo, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("tipo", tipo);
        values.put("usuario_id", usuarioId);
        db.insert("tareas", null, values);
    }
    public Cursor cargarTodas(int usuarioId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE usuario_id = ?", new String[]{String.valueOf(usuarioId)});
    }

    public Cursor cargarLeve(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ? AND usuario_id = ?", new String[]{"Leve", String.valueOf(usuarioId)});
    }

    public Cursor cargarModerada(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ? AND usuario_id = ?", new String[]{"Moderada", String.valueOf(usuarioId)});
    }

    public Cursor cargarUrgente(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ? AND usuario_id = ?", new String[]{"Urgente", String.valueOf(usuarioId)});
    }

    public void eliminarTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("tareas", "id=?", new String[]{id});
    }

    public void actualizarTask(String titulo, String descripcion, String tipo, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("tipo", tipo);
        db.update("tareas", values, "id=?", new String[]{id});
    }
}
