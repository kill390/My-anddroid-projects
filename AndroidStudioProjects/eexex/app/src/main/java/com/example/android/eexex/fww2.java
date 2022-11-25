package com.example.android.eexex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class fww2 extends AppCompatActivity {
    ListView li;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        TextView entvActNametwo = (TextView) findViewById(R.id.edit);
        TextView entvActNametwo2 = (TextView) findViewById(R.id.edit2);
        TextView entvActNametwo3 = (TextView) findViewById(R.id.edit3);
        TextView entvActNametwo4 = (TextView) findViewById(R.id.edit4);

        ListView listView = (ListView) findViewById(R.id.list_item);
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query(Contract.PetEntry.TABLE_NAME, null, null, null, null, null, null);

        CursorAdapter cursorAdapter = new cursorAdapter(this, cursor);

        listView.setAdapter(cursorAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Helper helper = new Helper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        switch (item.getItemId()) {


            case R.id.delete:// Respond to a click on the "Insert dummy data" menu option

                database.delete(Contract.PetEntry.TABLE_NAME, null, null);

        }
        return super.onOptionsItemSelected(item);

    }
}

