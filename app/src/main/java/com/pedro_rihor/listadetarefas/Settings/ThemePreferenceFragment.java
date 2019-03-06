package com.pedro_rihor.listadetarefas.Settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pedro_rihor.listadetarefas.R;

public class ThemePreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_theme);
    }
}
