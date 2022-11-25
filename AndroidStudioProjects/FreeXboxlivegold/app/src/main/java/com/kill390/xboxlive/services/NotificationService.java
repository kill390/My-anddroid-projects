package com.kill390.xboxlive.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kill390.xboxlive.AccountsActivity;
import com.kill390.xboxlive.DashboardActivity;
import com.kill390.xboxlive.R;

public class NotificationService extends Service {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Intent intent;
    PendingIntent pendingIntent;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

    @Override
    public void onCreate() {
        super.onCreate();

        startMyOwnForeground();


        final String msg = "Your account has been add come and check it now";

        createNotificationChannel();

        intent = new Intent(getApplicationContext(), AccountsActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Xbox live account")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("accounts").getValue() != null) {
                    if (!snapshot.child("accounts").child("email").getValue().toString().equals("null")) {

                        SharedPreferences sp = getSharedPreferences("com.kill390.xboxlive.services", Context.MODE_PRIVATE);
                        String email = sp.getString("email", "");

                        if (!email.equals(snapshot.child("accounts").child("email").getValue().toString())) {

                            SharedPreferences sp2 = getSharedPreferences("com.kill390.xboxlive.services", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp2.edit();
                            editor.putString("email", snapshot.child("accounts").child("email").getValue().toString()).apply();


                            pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setContentIntent(pendingIntent);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationService.this);

                            notificationManager.notify(0, builder.build());

                            Log.i("Notification", "Running");
                        }


                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.i("error",error.getMessage());

            }
        });



    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("com.kill390.xboxlive", Context.MODE_PRIVATE);

        boolean isChecked = sharedPreferences.getBoolean("isChecked",false);

        Log.i("service", String.valueOf(isChecked));

        if (isChecked) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, ServiceBrodcast.class);
            this.sendBroadcast(broadcastIntent);
        }else {
            stopForeground(true);
        }
//         Unregister the SMS receiver
//        unregisterReceiver(mSMSreceiver);
    }

    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "Notification service";
        String channelName = "Background Service";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        Intent intent = new Intent(this,DashboardActivity.class);
        pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        startForeground(2, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            String description = "accounts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
