package com.pedro_rihor.listadetarefas.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    final String STATE_CHANGE = "state_theme_change";
    final String THEME_PREFERENCE = "theme_preference";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean getThemeChanged() {
        return sharedPreferences.getBoolean(STATE_CHANGE, true);
    }

    public void setThemeChanged(boolean state) {
        editor.putBoolean(STATE_CHANGE, state);
        editor.apply();
    }

    public String getTheme() {
        return sharedPreferences.getString(THEME_PREFERENCE, "Azul");
    }

    public void setTheme(String themeName) {
        editor.putString(THEME_PREFERENCE, themeName);
        editor.apply();
    }
}
