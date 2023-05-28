package com.example.jabsa.alarmNotification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear un nuevo intent para iniciar el servicio de notificación
        Intent service1 = new Intent(context, NotificationService.class);
        // Establecer datos personalizados en la intención del servicio
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        // Iniciar el servicio en primer plano utilizando ContextCompat para garantizar la compatibilidad
        ContextCompat.startForegroundService(context, service1 );

    }
}
