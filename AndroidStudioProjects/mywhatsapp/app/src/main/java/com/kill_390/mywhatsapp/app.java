package com.kill_390.mywhatsapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

public class app extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("ITIWU1n6CZLXpKe5GsRXZrMuOywtiv0ao3ma2Xzz")
                .clientKey("oFZPNLEqUSxQII8xujhBgvwcWkiNkIeq5RzaSCm4")
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
