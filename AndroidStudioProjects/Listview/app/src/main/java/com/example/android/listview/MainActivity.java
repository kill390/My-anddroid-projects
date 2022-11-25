package com.example.android.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> arrayList;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       listView = findViewById(R.id.list_view);

        SeekBar seekBar = findViewById(R.id.seekBar);

        seekBar.setMin(1);

        seekBar.setMax(21);

        seekBar.setProgress(5);

        oo(seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                oo(progress);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    private void oo(int progress){

        arrayList = new ArrayList<Integer>();

        for (int x =1 ; x<=15 ; x++ ){

            arrayList.add(x*progress);

        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(MainActivity.this , android.R.layout.simple_list_item_1 , arrayList);

        listView.setAdapter(arrayAdapter);
    }
}
