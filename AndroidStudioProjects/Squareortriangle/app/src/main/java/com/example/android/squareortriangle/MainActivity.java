package com.example.android.squareortriangle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public void check(View view){
        EditText editText =(EditText)findViewById(R.id.editText);
        String st = editText.getText().toString();
        int number = Integer.parseInt(st);
        numberr num = new numberr();
        if(num.checkPerfectSquare(number)&&num.isTriangularNumber(number)){
            Toast.makeText(this, "the number: "+ number +" is a square  and triangular number", Toast.LENGTH_LONG).show();


        }
        if (num.checkPerfectSquare(number)){
            Toast.makeText(this, "the number: "+ number +" is a square number", Toast.LENGTH_LONG).show();

        }else if(num.isTriangularNumber(number)){

            Toast.makeText(this, "the number: " + number + " is a Triangular Number", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "the number: " + number + " not a Triangular or square number ", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    class numberr {
        public boolean checkPerfectSquare(double x) {

            // finding the square root of given number
            double sq = Math.sqrt(x);

            /* Math.floor() returns closest integer value, for
             * example Math.floor of 984.1 is 984, so if the value
             * of sq is non integer than the below expression would
             * be non-zero.
             */
            return ((sq - Math.floor(sq)) == 0);
        }

        public  boolean isTriangularNumber(long num) {
            long calc_num = 8 * num + 1;
            long t = (long) Math.sqrt(calc_num);
            if (t * t == calc_num) {
                return true;
            }
            return false;
        }
    }
}
