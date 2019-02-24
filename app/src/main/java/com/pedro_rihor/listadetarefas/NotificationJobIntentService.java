package com.pedro_rihor.listadetarefas;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class NotificationJobIntentService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        System.out.println("************** NotificationJobIntentService online");
        Tarefa tarefa = intent.getParcelableExtra(MainActivity.EXTRA_TAREFA);
        Notification notification = intent.getParcelableExtra(MainActivity.EXTRA_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
