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
        if (intent.getAction().equals(MY_ACTION)) {
            NotificationService.mMensaje = intent.getStringExtra("tarea");
        }
    }
}