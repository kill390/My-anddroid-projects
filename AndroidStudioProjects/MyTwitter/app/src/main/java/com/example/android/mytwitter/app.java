package com.example.android.mytwitter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("WvTECJTf1gYLNMBOUbOylb4hMm3MGchaGC3D5GvF")
                .clientKey("kG1fRKuVCCxLRuE1vZuxgxRd7LYCAUwmjYIzIWEL")
                .server("https://parseapi.back4app.com/")
                .build()
        );



        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
