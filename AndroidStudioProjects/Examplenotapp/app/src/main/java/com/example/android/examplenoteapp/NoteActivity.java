package com.example.android.examplenoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class NoteActivity extends AppCompatActivity {


    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("index",-1);

        if (noteId != -1) {
            editText.setText(MainActivity.arrayList.get(noteId));
        } else {
            MainActivity.arrayList.add("");
            noteId = MainActivity.arrayList.size() - 1;
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.arrayList.set(noteId, String.valueOf(charSequence));
                MainActivity.adapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.examplenoteapp", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
