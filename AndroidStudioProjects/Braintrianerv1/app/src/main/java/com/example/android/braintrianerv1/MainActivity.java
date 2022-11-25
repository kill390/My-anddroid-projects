package com.example.android.braintrianerv1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView go ;
    TextView timer;
    TextView quiz ;
    TextView total ;
    TextView results ;
    TextView answer1 ;
    TextView answer2 ;
    TextView answer3 ;
    TextView answer4 ;
    Button button ;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    public void go(View view){
        setvisible();
        timer();
        newQuestion();

    }
    public void tryagain(View view){
        timer();
        button.setVisibility(View.INVISIBLE);
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);
        answer4.setEnabled(true);
        total.setText("0/0");
        results.setVisibility(View.INVISIBLE);
        score=0;
        numberOfQuestions=0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer=findViewById(R.id.timer);
        quiz=findViewById(R.id.quiz);
        total=findViewById(R.id.total);
        results=findViewById(R.id.result);
        answer1=findViewById(R.id.answer1);
        answer2=findViewById(R.id.answer2);
        answer3=findViewById(R.id.answer3);
        answer4=findViewById(R.id.answer4);
        go=findViewById(R.id.go);
        button=findViewById(R.id.bt);


    }
    public void setvisible(){
        go.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.VISIBLE);
        quiz.setVisibility(View.VISIBLE);
        total.setVisibility(View.VISIBLE);
        answer1.setVisibility(View.VISIBLE);
        answer2.setVisibility(View.VISIBLE);
        answer3.setVisibility(View.VISIBLE);
        answer4.setVisibility(View.VISIBLE);

    }
    public void timer(){
        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.tik);
        new CountDownTimer (30000+100,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Integer.toString((int) (millisUntilFinished/1000)));


                mediaPlayer.start();

            }

            @Override
            public void onFinish() {
                button.setVisibility(View.VISIBLE);
                answer1.setEnabled(false);
                answer2.setEnabled(false);
                answer3.setEnabled(false);
                answer4.setEnabled(false);
                results.setVisibility(View.VISIBLE);
                results.setText("done !\nwith total: "+total.getText().toString());
            }
        }.start();
    }
    boolean isclicke =false;
    int numberOfQuestions = 0;
    int score = 0 ;
    String cn;
    int locationOfCorrectAnswer ;
    public void chooseAnswer(View view) {
        results.setVisibility(View.VISIBLE);
        if (Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())) {
            results.setText("Correct!");
            score++;
        } else {
            results.setText("Wrong :(");
        }
        numberOfQuestions++;
        total.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestions));
        newQuestion();
    }


    public void newQuestion() {
        Random rand = new Random();

        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        quiz.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);

        answers.clear();

        for (int i=0; i<4; i++) {
            if (i == locationOfCorrectAnswer) {
                answers.add(a+b);
            } else {
                int wrongAnswer = rand.nextInt(41);

                while (wrongAnswer == a+b) {
                    wrongAnswer = rand.nextInt(41);
                }

                answers.add(wrongAnswer);
            }

        }

        answer1.setText(answers.get(0).toString());
        answer2.setText(answers.get(1).toString());
        answer3.setText(answers.get(2).toString());
        answer4.setText(answers.get(3).toString());
    }



}
