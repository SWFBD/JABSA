package com.example.jabsa.alarmNotification;

import static com.example.jabsa.TareaFragment.MY_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public MyBroadcastReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // Verificar si la acción del intent coincide con la acción personalizada
        if (intent.getAction().equals(MY_ACTION)) {
            // Obtener el mensaje de tarea extraído del intent y asignarlo a la variable mMensaje de NotificationService
            NotificationService.mMensaje = intent.getStringExtra("tarea");
        }
    }
}