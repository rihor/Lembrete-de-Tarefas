package com.pedro_rihor.listadetarefas.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.pedro_rihor.listadetarefas.MainActivity;
import com.pedro_rihor.listadetarefas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ShowNotificationJob extends Job {

    static final String TAG = "show_notification_job_tag";
    static final String CHANNEL_ID = "canal1";

    ShowNotificationJob() {
        super();
        System.out.println("TESTE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public static void scheduleNotification(int hora, int minuto) {
        System.out.println("ShowNotificationJob scheduleNotification online !!!!!!!!!!!!!!!!!!!!!");


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        calendar.set(Calendar.HOUR, hora);
        calendar.set(Calendar.MINUTE, minuto);

        System.out.println("Hora: " + hora + " !!!!!");
        System.out.println("Minuto: " + minuto + " !!!!!");

        // calcula o delay necessario
        long delayExato = calendar.getTimeInMillis() - System.currentTimeMillis();
        System.out.println("delay: " + delayExato + " !!!!!");

        new JobRequest.Builder(ShowNotificationJob.TAG)
                //.setPeriodic(TimeUnit.MINUTES.toMillis(1), TimeUnit.MINUTES.toMillis(1))
                .setExact(delayExato) // funciona apenas com delay
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setContentTitle("Tarefa")
                .setContentText("Você tem tarefas a fazer!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_stat_notification)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);
        System.out.println("ShowNotificationJob onRunJob online !!!!!!!!!!!!!!!!!!!!!");
        return Result.SUCCESS;
    }
}
