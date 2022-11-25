package com.example.experment;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.nio.channels.Channel;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Calendar.DATE;
import static java.util.Calendar.PM;

public class MainActivity extends AppCompatActivity {
    boolean ispaused = false ;
    boolean iscliked = false ;
    boolean isplayed = false ;
    public void play (View view){
        if(isplayed){
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
        mediaPlayer.start();
        isplayed=true;
    }
    public void pause (View view){
        mediaPlayer.pause();
        ispaused=true ;

    }
    MediaPlayer mediaPlayer ;
    AudioManager audioManager ;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager =(AudioManager) getSystemService(AUDIO_SERVICE);

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_1);

        SeekBar seekBar =(SeekBar) findViewById(R.id.volumeSeekBar);

        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setProgress(currentVolume);

        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar seekBar1 = (SeekBar)findViewById(R.id.seekBar);
        seekBar1.setMax(mediaPlayer.getDuration());
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {

                    mediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();

            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                    int me = mediaPlayer.getCurrentPosition();

                    seekBar1.setProgress(me);


            }
        },0,100);

        
    }

}