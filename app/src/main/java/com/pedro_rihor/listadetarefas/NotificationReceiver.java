package com.pedro_rihor.listadetarefas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

// broadcast receiver dura em torno de 10 segundos apenas, portanto deve-se lançar a notificação de um serviço
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("**************************** Receiver online");
        Intent intentService = new Intent(context, NotificationJobIntentService.class);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            intentService.putExtras(intent.getExtras());
        }
        context.startService(intentService);

    }
}
