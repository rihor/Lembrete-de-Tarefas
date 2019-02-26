package com.pedro_rihor.listadetarefas.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.pedro_rihor.listadetarefas.MainActivity;
import com.pedro_rihor.listadetarefas.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationHandler extends Worker {
    public NotificationHandler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void scheduleNotification(long delay, Data data, String tag) {
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationHandler.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();

        WorkManager.getInstance().enqueue(notificationWork);
    }

    public static void cancelNotification(String tag) {
        WorkManager instance = WorkManager.getInstance();
        instance.cancelAllWorkByTag(tag);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        String title = getInputData().getString(Constants.EXTRA_TITLE);
        String text = getInputData().getString(Constants.EXTRA_TEXT);
        int id = (int) getInputData().getLong(Constants.EXTRA_ID, 0);

        sendNotification(title, text, id);
        return Result.success();
    }

    private void sendNotification(String title, String text, int id) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constants.EXTRA_ID, id);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "canal1")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_stat_notification)
                .setColor(Color.RED)
                .setAutoCancel(true);

        Objects.requireNonNull(notificationManager).notify(id, notification.build());
    }

//    @NonNull
//    @Override
//    public Result doWork() {
//        System.out.println("NotificationHandler doWork online !!!!!!!!!!!!!!!!!!!!!");
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setContentTitle("Tarefa")
//                .setContentText("Você tem tarefas a fazer!")
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.ic_stat_notification)
//                .setShowWhen(true)
//                .setColor(Color.RED)
//                .setLocalOnly(true)
//                .build();
//
//        NotificationManagerCompat.from(getApplicationContext()).notify(new Random().nextInt(), notification);
//        return Worker.Result.success();
//    }success
}
