package com.example.android.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    public void media(View view){

        Button button = (Button) view;

       String tag = button.getTag().toString();


        mediaPlayer = MediaPlayer.create(this,getResources().getIdentifier(tag, "raw", getPackageName()));

         mediaPlayer.start();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
