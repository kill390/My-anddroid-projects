package com.kill390.smsreadtest;

import android.app.Application;
import android.content.IntentFilter;
import android.provider.Telephony;

public class app extends Application {
    private SmsReceiver smsBroadcastReceiver;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        smsBroadcastReceiver = new SmsReceiver(BuildConfig.SERVICE_NUMBER, BuildConfig.SERVICE_CONDITION);
//        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));
//    }
//
//    @Override
//    public void onTerminate() {
//        unregisterReceiver(smsBroadcastReceiver);
//        super.onTerminate();
//    }
}
