package com.example.hortitechv1.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.hortitechv1.models.LoginResponse;
import com.example.hortitechv1.models.Persona;
import com.example.hortitechv1.view.MainActivity;

public class SessionManager {
    private static final String PREF_NAME = "HORTITECH_PREFS";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final String KEY_USER_TOKEN = "USER_TOKEN";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_EMAIL = "USER_EMAIL";
    private static final String KEY_USER_ROL = "USER_ROL";
    private static final String KEY_USER_FOTO_URL = "USER_FOTO_URL";

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserSession(LoginResponse loginResponse) {
        String token = loginResponse.getToken();
        Persona user = loginResponse.getUser();

        editor.putString(KEY_USER_TOKEN, "Bearer " + token);
        if (user != null) {
            editor.putInt(KEY_USER_ID, user.getId_persona());
            editor.putString(KEY_USER_NAME, user.getNombre_usuario());
            editor.putString(KEY_USER_EMAIL, user.getCorreo());
            // Gson convierte el enum a String, lo guardamos como tal
            if(user.getRol() != null) {
                editor.putString(KEY_USER_ROL, user.getRol().name());
            }
            if (user.getPerfil() != null) {
                editor.putString(KEY_USER_FOTO_URL, user.getPerfil().getFoto_url());
            }
        }
        editor.apply();
    }

    public String getAuthToken() { return prefs.getString(KEY_USER_TOKEN, null); }
    public int getUserId() { return prefs.getInt(KEY_USER_ID, -1); }
    public String getUserName() { return prefs.getString(KEY_USER_NAME, null); }
    public String getUserEmail() { return prefs.getString(KEY_USER_EMAIL, null); }
    public String getUserRol() { return prefs.getString(KEY_USER_ROL, null); }
    public String getUserFotoUrl() { return prefs.getString(KEY_USER_FOTO_URL, null); }

    public void logoutUser() {
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}