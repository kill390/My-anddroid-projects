    package com.example.android.examplenoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

    public class MainActivity extends AppCompatActivity {

        static ArrayList<String> arrayList = new ArrayList<String>();


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu,menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){

                case R.id.newNote:
                    Intent intent = new Intent(this,NoteActivity.class);
                    startActivity(intent);

            }

            return super.onOptionsItemSelected(item);
        }
        static ArrayAdapter<String> adapter ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.examplenoteapp", Context.MODE_PRIVATE);

            ListView listView = findViewById(R.id.listView);
            HashSet<String> o = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
            if (o==null){
                arrayList.add("example note");

            }else{
                arrayList=new ArrayList<>(o);
            }


            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent =new Intent(MainActivity.this,NoteActivity.class);
                    intent.putExtra("index",position);

                    startActivity(intent);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    arrayList.remove(position);
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.examplenoteapp", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                    adapter.notifyDataSetChanged();


                    return false;
                }
            });


        }
}
