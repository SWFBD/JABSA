package com.example.jabsa.alarmNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MyRebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear un nuevo intent para iniciar el servicio de reinicio
        Intent serviceIntent = new Intent(context, RebootServiceClass.class);
        // Agregar un extra a la intención para identificar al receptor que llamó al servicio
        serviceIntent.putExtra("caller", "RebootReceiver");
        // Comprobar la versión de Android para determinar qué método de inicio del servicio se debe utilizar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Iniciar el servicio en primer plano si la versión de Android es mayor o igual a Oreo (API 26)
            context.startForegroundService(serviceIntent);
        } else {
            // Iniciar el servicio si la versión de Android es anterior a Oreo (API 26)
            context.startService(serviceIntent);
        }
    }
}