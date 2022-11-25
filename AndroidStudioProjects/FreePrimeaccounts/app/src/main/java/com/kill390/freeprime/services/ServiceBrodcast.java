package com.kill390.freeprime.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ServiceBrodcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("restartservice")) {
            Log.i("service", "restart0");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, NotificationService.class));
            } else {
                context.startService(new Intent(context, NotificationService.class));
            }
        }
    }
}
