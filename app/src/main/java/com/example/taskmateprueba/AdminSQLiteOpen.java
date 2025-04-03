package com.example.taskmateprueba;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
public class AdminSQLiteOpen extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "BdAppToDo.db";
    private static final int DATABASE_VERSION = 1;

    public AdminSQLiteOpen(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Habilitamos el soporte de claves foráneas
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla "usuarios"
        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    nombre_usuario TEXT NOT NULL UNIQUE," +
                        "    correo TEXT NOT NULL UNIQUE," +
                        "    contrasena TEXT NOT NULL" +
                        ");"
        );

        // Crear tabla "tareas"
        db.execSQL(
                "CREATE TABLE tareas (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    id_usuario INTEGER," +
                        "    titulo TEXT NOT NULL," +
                        "    descripcion TEXT," +
                        "    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "    fecha_programada DATE," +
                        "    completada INTEGER DEFAULT 0," +
                        "    prioridad INTEGER CHECK(prioridad IN (1, 2, 3))," +
                        "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                        ");"
        );

        // Crear tabla "tareas_diarias"
        db.execSQL(
                "CREATE TABLE tareas_diarias (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    id_tarea INTEGER," +
                        "    fecha DATE NOT NULL," +
                        "    completada INTEGER DEFAULT 0," +
                        "    FOREIGN KEY(id_tarea) REFERENCES tareas(id) ON DELETE CASCADE" +
                        ");"
        );

        // Crear tabla "tareas_recurrentes"
        db.execSQL(
                "CREATE TABLE tareas_recurrentes (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    id_usuario INTEGER," +
                        "    titulo TEXT NOT NULL," +
                        "    descripcion TEXT," +
                        "    prioridad INTEGER CHECK(prioridad IN (1, 2, 3))," +
                        "    hora TEXT, " +  // SQLite no tiene un tipo TIME nativo; se usa TEXT o NUMERIC según convenga
                        "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                        ");"
        );

        // Crear tabla "calendario"
        db.execSQL(
                "CREATE TABLE calendario (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    id_tarea INTEGER," +
                        "    fecha DATE NOT NULL," +
                        "    FOREIGN KEY(id_tarea) REFERENCES tareas(id) ON DELETE CASCADE" +
                        ");"
        );

        // Crear tabla "diario"
        db.execSQL(
                "CREATE TABLE diario (" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "    id_usuario INTEGER," +
                        "    fecha DATE NOT NULL," +
                        "    contenido TEXT," +
                        "    estado_animo TEXT," +
                        "    FOREIGN KEY(id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                        ");"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS diario");
        db.execSQL("DROP TABLE IF EXISTS calendario");
        db.execSQL("DROP TABLE IF EXISTS tareas_recurrentes");
        db.execSQL("DROP TABLE IF EXISTS tareas_diarias");
        db.execSQL("DROP TABLE IF EXISTS tareas");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
}