package com.example.android.instgramclone;

import android.app.Application;

import com.parse.Parse;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("rnPfIG30gfvxDG4wjN7sP2STTw8U9yx0DYrpRuDr")
                .clientKey("EXGZx68VkZWPVxK9bnLmahwLYOuVa7iSsxszXZ59")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
