package com.example.android.countdountimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.PublicKey;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {



    int progress;
    TextView textView;
    CountDownTimer countDownTimer;
    Boolean isclicked =false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar seekBar = findViewById(R.id.seekBar);
         textView= findViewById(R.id.text);


        seekBar.setMin(0);
        seekBar.setMax(300);
        seekBar.setProgress(30);



        textView.setText(00+":"+30);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = progress/60;
                int sec = progress-(min*60) ;

                String st = Integer.toString(sec);
                if (sec<=9){
                    st = "0"+sec ;
                }

                textView.setText(min+":"+st);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (isclicked) {

                    countDownTimer.cancel();
                    isclicked=false ;
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final Button button = findViewById(R.id.button12);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setEnabled(false);
                button.setText("cancel");

                if(isclicked==true){
                    countDownTimer.cancel();
                    button.setText("Go!");
                    seekBar.setEnabled(true);
                    isclicked=false;
                }else{

                progress= seekBar.getProgress();
                 countDownTimer = new CountDownTimer(progress*1000+100,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        seekBar.setProgress((int) (millisUntilFinished/1000));
                        int min = (int) (millisUntilFinished/60000);
                        int sec = (int) (millisUntilFinished/1000-(min*60));
                        String st = Integer.toString(sec);
                        if (sec<=9){
                            st = "0"+sec ;
                        }
                        isclicked=true;



                        textView.setText(min + ":" +st );


                    }

                    @Override
                    public void onFinish() {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.air_horn);
                        mediaPlayer.start();
                        button.setText("Go!");
                        seekBar.setEnabled(true);


                    }

                };

                 countDownTimer.start();

            }}

        });
    }
}

















//    final Handler handler = new Handler();

//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                handler.postDelayed(this , 1000);
//
//                Log.i("handler","runnable");
//
//
//            }
//        };
//        handler.post(runnable);
