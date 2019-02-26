package com.pedro_rihor.listadetarefas.Notification;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class TarefaJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        System.out.println("TarefaJobCreator create online !!!!!!!!!!!!!!!!!!!!!");

        switch (tag) {
            case ShowNotificationJob.TAG:
                return new ShowNotificationJob();
            default:
                return null;
        }
    }
}
