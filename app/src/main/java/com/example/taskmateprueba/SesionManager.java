package com.example.taskmateprueba;

import android.content.Context;
import android.content.SharedPreferences;

public class SesionManager {

    private static final String PREF_NAME = "SesionPref";
    private static final String KEY_USUARIO_ID = "usuario_id";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SesionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void guardarUsuarioId(int usuarioId) {
        editor.putInt(KEY_USUARIO_ID, usuarioId);
        editor.apply();
    }

    public int obtenerUsuarioId() {
        return prefs.getInt(KEY_USUARIO_ID, -1);
    }

    public boolean haySesionActiva() {
        return prefs.contains(KEY_USUARIO_ID);
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }
}
