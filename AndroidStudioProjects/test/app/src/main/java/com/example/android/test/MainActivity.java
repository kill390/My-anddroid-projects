package com.example.android.test;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    public void kind(View view){
        if (getResources().getConfiguration().isScreenRound()){
            Toast.makeText(this, "screen is round", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "screen is square", Toast.LENGTH_SHORT).show();
        }
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
