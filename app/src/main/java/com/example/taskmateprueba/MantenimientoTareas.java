package com.example.taskmateprueba;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteQuery;
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
        SQLiteDatabase.execSQL("Create Table my_table(id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT,descripcion TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDatabase, int i, int ii) {
        SQLiteDatabase.execSQL("drop table if exists my_table");
    }

    public void insertarDatos(String titulo, String descripcion){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Titulo",titulo);
        values.put("Descripcion",descripcion);
        database.insert("my_table",null,values);
    }

}
