package com.example.android.animate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    int alpha;
    ImageView imageView ;
    ImageView imageView2 ;

    boolean image =true ;
    public void animate(View view) {
       imageView = (ImageView)findViewById(R.id.imageView);
       imageView2 = (ImageView)findViewById(R.id.imageView2);
       imageView.animate().alpha(1).scaleXBy(1).rotation(3600).setDuration(2000);
       imageView.animate().alpha()
       imageView.setVisibility(View.VISIBLE);




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.animate().alpha(0);


    }
}
