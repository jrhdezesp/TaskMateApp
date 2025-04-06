package com.example.taskmateprueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MantenimientoTareas extends SQLiteOpenHelper {

    private static final String DATABASE = "DATABASE";
    private static final int VERSION = 2;

    public MantenimientoTareas(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase SQLiteDatabase) {
        SQLiteDatabase.execSQL("Create Table my_table(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDatabase, int i, int ii) {
        SQLiteDatabase.execSQL("drop table if exists my_table");
        onCreate(SQLiteDatabase); // Recreate the table after dropping
    }

    public void insertarDatos(String _titulo, String _descripcion) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", _titulo);
        values.put("descripcion", _descripcion);
        database.insert("my_table", null, values);
    }

    public Cursor cargarDatos() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("Select * from my_table", null);
    }

    public void eliminarDatos(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("my_table","id=?", new String[]{id});
    }

    public void actualizarDatos(String titulo, String descripcion,String id){
        SQLiteDatabase SqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("titulo",titulo);
        values.put("descripcion",descripcion);

        SqLiteDatabase.update("my_table",values,"id=?",new String[]{id});

    }

}