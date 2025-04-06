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
        // Tabla Journal
        db.execSQL("CREATE TABLE journal(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT)");

        // Tabla My_Table
        db.execSQL("CREATE TABLE my_table(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Puedes mejorar esto para hacer migraciones más controladas
        db.execSQL("DROP TABLE IF EXISTS journal");
        db.execSQL("DROP TABLE IF EXISTS my_table");
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

    // Métodos para my_table
    public void insertarDiaria(String titulo, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.insert("my_table", null, values);
    }

    public Cursor cargarDiarias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM my_table", null);
    }

    public void eliminarDiaria(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("my_table", "id=?", new String[]{id});
    }

    public void actualizarDiaria(String titulo, String descripcion, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descripcion", descripcion);
        db.update("my_table", values, "id=?", new String[]{id});
    }
}