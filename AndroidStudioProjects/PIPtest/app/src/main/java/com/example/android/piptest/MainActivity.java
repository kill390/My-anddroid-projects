package com.example.android.piptest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void enterPip(View view){
        enterPictureInPictureMode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if (isInPictureInPictureMode){
            getActionBar().hide();

        }else{
            getActionBar().show();
        }
    }
}
