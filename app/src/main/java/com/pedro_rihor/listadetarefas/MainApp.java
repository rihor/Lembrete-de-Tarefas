package com.pedro_rihor.listadetarefas;

import android.app.Application;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MainApp onCreate online!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
