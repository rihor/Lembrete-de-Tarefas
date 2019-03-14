package com.pedro_rihor.listadetarefas.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.pedro_rihor.listadetarefas.R;

public class SettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    SharedPref sharedPref;
    int check = 0; // evitar bug de seleção estantanea
    private Spinner spinnerThemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        int positionSpinner = loadTheme(); // define o tema de acordo com a preferencia guardada
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinnerThemes = findViewById(R.id.spinner);
        spinnerThemes.setOnItemSelectedListener(this);
        spinnerThemes.setSelection(positionSpinner);
    }

    public int loadTheme() {
        switch (sharedPref.getTheme()) {
            case Themes.VERDE:
                setTheme(R.style.GreenTheme);
                return 1;
            case Themes.BRANCO:
                setTheme(R.style.WhiteTheme);
                return 2;
            case Themes.PRETO:
                setTheme(R.style.BlackTheme);
                return 3;
            case Themes.AZUL:
            default:
                setTheme(R.style.BlueTheme);
                return 0;
        }
    }

    private void restartApp() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (++check > 1) { // evitar bug de seleção instantanea
            Log.d("Theme", parent.getItemAtPosition(position).toString() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            sharedPref.setTheme(spinnerThemes.getSelectedItem().toString());
            sharedPref.setThemeChanged(true);
            restartApp();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
