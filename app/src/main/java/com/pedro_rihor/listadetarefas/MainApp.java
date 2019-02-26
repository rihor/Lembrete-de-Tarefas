package com.pedro_rihor.listadetarefas;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.pedro_rihor.listadetarefas.Notification.TarefaJobCreator;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MainApp onCreate online!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        JobManager.create(this).addJobCreator(new TarefaJobCreator());
        JobManager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true); // Não usar quando lançar
    }
}
