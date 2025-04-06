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

        //Tabla Usuarios
        db.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, clave TEXT, correo TEXT)");
        // Tabla Journal
        db.execSQL("CREATE TABLE journal(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT)");
        //Tabla diarias
        db.execSQL("CREATE TABLE diarias(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT)");
        //Tabla Tareas
        db.execSQL("CREATE TABLE tareas(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, tipo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Puedes mejorar esto para hacer migraciones más controladas
        db.execSQL("DROP TABLE IF EXISTS journal");
        db.execSQL("DROP TABLE IF EXISTS diarias");
        onCreate(db);
    }

    // Métodos para journal
    public void insertarNota(String titulo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.insert("journal", null, values);
    }

    public Cursor cargarNotas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM journal", null);
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

    // Métodos para diarias
    public void insertarDiaria(String titulo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.insert("diarias", null, values);
    }

    public Cursor cargarDiarias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM diarias", null);
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

    //Tasks

    public void insertarTask(String titulo, String descripcion, String tipo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        values.put("tipo",tipo);
        db.insert("tareas", null, values);
    }

    public Cursor cargarLeve() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ?", new String[]{"Leve"});
    }

    public Cursor cargarModerada() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ?", new String[]{"Moderada"});
    }

    public Cursor cargarUrgente() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tareas WHERE tipo = ?", new String[]{"Urgente"});
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