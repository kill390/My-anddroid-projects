package com.example.android.howmanypeople;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {
    int number = 0 ;

    public void plus(View view){
        number++;
        mTextView.setText(String.valueOf(number));
    }
    public void reset(View view){
        number=0;
        mTextView.setText(String.valueOf(number));
    }

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
    }
}
