package com.example.android.higherorlower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    int random ;


    public void Guess(View view){

        Log.i("info","the number is "+random);
        EditText editText =(EditText)findViewById(R.id.editText);


         if(editText.getText().toString().isEmpty()){
             Toast.makeText(this, "pleas enter a number", Toast.LENGTH_SHORT).show();

             return;
         }


        int number = Integer.parseInt(editText.getText().toString());
        if(random==number){
            Toast.makeText(this, "that's right try again", Toast.LENGTH_SHORT).show();
            random =new Random().nextInt(20)+1;

        }else if(number < random){
            Toast.makeText(this, "I am Higher", Toast.LENGTH_SHORT).show();

        }else if(number > random){
            Toast.makeText(this, "I am Lower", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         random = new Random().nextInt(20)+1;
    }
}
