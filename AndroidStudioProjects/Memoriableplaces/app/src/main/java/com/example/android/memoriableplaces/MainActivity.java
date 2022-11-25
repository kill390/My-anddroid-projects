package com.example.android.memoriableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<String> arrayList = new ArrayList<String>();
    public static ArrayList<LatLng> latLangArrayList = new ArrayList<LatLng>();

    public static ArrayAdapter<String> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.memoriableplaces", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("arrayPlaces", ObjectSerializer.serialize(arrayList.toString())).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }


        ListView listView = findViewById(R.id.listView);

//        try {
//            ArrayList<String> arrayList2=new ArrayList<String>();
//            arrayList2 = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("arrayPlaces",""));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        LatLng latLng = new LatLng(0,0);
        latLangArrayList.add(latLng);
        arrayList.add("add a new place");


         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("number",position);
                startActivity(intent);


            }
        });

    }
}
