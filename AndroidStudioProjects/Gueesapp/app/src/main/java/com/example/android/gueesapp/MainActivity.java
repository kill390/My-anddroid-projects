package com.example.android.gueesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();


                while (data!=-1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();

                return "Failed";
            }
        }
    }





    ImageView imageView ;

    ArrayList<String> array = new ArrayList<String>();
    ArrayList<String> url = new ArrayList<String>();
    String result ;
    Button button1 ;
    Button button2 ;
    Button button3 ;
    Button button4 ;
    int num ;
    ArrayList<String> names = new ArrayList<String>();


    public void guess(View view) {
        Button button = (Button) view;
        String tag = button.getTag().toString();
        Log.i("ooo","hee:"+tag);
        if (tag.equals(Integer.toString(num))){
            Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
            anny();

        }else{
            Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show();
        }


    }
    public void anny (){
    Random random = new Random();
     num = random.nextInt(4);
    int num2 = random.nextInt(names.size());
    array.clear();




    for (int i=0; i<4; i++) {
        if (num==i){
            array.add(names.get(num2));
            Picasso.get().load(url.get(num2)).into(imageView);


            Log.i("ooo","heere"+names.get(num2)+url.get(num2));

        }else {
            int num3 = random.nextInt(names.size());

            while (num3 == num2){
                 num3 = random.nextInt(names.size());
            }
            array.add(names.get(num3));

        }


    }

        button1.setText(array.get(0));
        button2.setText(array.get(1));
        button3.setText(array.get(2));
        button4.setText(array.get(3));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         imageView =findViewById(R.id.image);

        button1=findViewById(R.id.button);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);


        DownloadTask task = new DownloadTask();
            try {
            result = task.execute("http://cdn.posh24.se/kandisar").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("<img src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(result);
        Pattern pattern2 = Pattern.compile("alt=\"(.*?)\"");
        Matcher matcher2 = pattern2.matcher(result);

        while ( matcher.find() && matcher2.find()) {

            names.add(matcher2.group(1));
            url.add(matcher.group(1));

        }
        Log.i("lll", "here " + names.toString() + url.toString());

        anny();

    }
}