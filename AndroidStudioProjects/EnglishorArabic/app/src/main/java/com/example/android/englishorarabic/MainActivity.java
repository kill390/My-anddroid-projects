package com.example.android.englishorarabic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem arabic = menu.findItem(R.id.Arabic);
        MenuItem english = menu.findItem(R.id.English);
        boolean isEnglish =   sharedPreferences.getBoolean("english",false);
        boolean isArabic =   sharedPreferences.getBoolean("arabic",false);

        Log.i("english + arabic",isEnglish+":"+isArabic);

        if (isEnglish){
            english.setChecked(true);
            arabic.setChecked(false);
        }
        if (isArabic){
            arabic.setChecked(true);
            english.setChecked(false);
        }


        return super.onPrepareOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean isEnglish =   sharedPreferences.getBoolean("english",false);
        boolean isArabic =   sharedPreferences.getBoolean("arabic",false);

        if (isEnglish){
           sharedPreferences.edit().putBoolean("arabic",false).apply();
        }
        if (isArabic){
            sharedPreferences.edit().putBoolean("english",false).apply();
        }


        switch (item.getItemId()){
            case R.id.English :
                item.setChecked(true);
                sharedPreferences.edit().putBoolean("arabic",false).apply();

            case R.id.Arabic :
                item.setChecked(true);
                sharedPreferences.edit().putBoolean("english",false).apply();


        }


        return super.onOptionsItemSelected(item);
    }

    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sharedPreferences = this.getSharedPreferences("com.example.android.englishorarabic", Context.MODE_PRIVATE);

        boolean firstOpen = true ;
//                sharedPreferences.getBoolean("boolean",true) ;
        if (firstOpen) {

//            sharedPreferences.edit().putBoolean("boolean",false).apply();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setTitle("which language")
                    .setMessage("English or Arabic")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putBoolean("english",true).apply();
                            sharedPreferences.edit().putBoolean("arabic",false).apply();

                        }
                    })
                    .setNegativeButton("Arabic", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sharedPreferences.edit().putBoolean("arabic",true).apply();
                            sharedPreferences.edit().putBoolean("english",false).apply();
                        }
                    });
            alertDialog.show();
        }
    }
}
