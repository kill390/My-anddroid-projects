package com.example.android.forecastweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public class WeatherTask extends AsyncTask<String , Void , String>{


        @Override
        protected String doInBackground(String... urls) {
            String result ="" ;
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char curent = (char) data;
                    result += curent ;
                    data = reader.read() ;

                }
                return result ;


            } catch (Exception e) {
                e.printStackTrace();

                return "failed" ;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("failed")){
                Toast.makeText(MainActivity.this, "write a correct city", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Log.i("s", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray array = jsonObject.getJSONArray("weather");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        main = object.getString("main");
                        description = object.getString("description");
                        Log.i("here", main + description);
                        textView.setText(main + " : " + description);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    String main ;

    String description ;

    public void get(View view) throws UnsupportedEncodingException {
        WeatherTask task = new WeatherTask() ;
        Log.i("s","here"+editText.getText().toString());

        if (editText.getText().toString().equals("")){
            Toast.makeText(this, "enter a city", Toast.LENGTH_SHORT).show();
            return;
        }else {
            String country = URLEncoder.encode(editText.getText().toString().trim(),"UTF-8");
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + country + "&appid=2b4887d4e016659cbf673f65db8dbe32");
        }
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
    TextView textView ;
    EditText editText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         editText = findViewById(R.id.editText);
        textView = findViewById(R.id.text);





    }
}
