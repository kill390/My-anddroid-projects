package com.example.android.showinghidinguielements;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public void show (View view){
        textVeiw.setVisibility(View.VISIBLE);

    }
    public void hide (View view){
        textVeiw.setVisibility(View.INVISIBLE);

    }
     TextView textVeiw ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textVeiw = findViewById(R.id.text);
    }
}
