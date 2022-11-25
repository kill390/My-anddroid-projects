
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.android.pets.data.cotract.PetEntry;
import com.example.android.pets.data.petDbHelper;



import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PetCursorAdapter cursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }


        });


        ListView displayView = (ListView) findViewById(R.id.List_view_pet);

        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent open = new Intent(CatalogActivity.this,EditorActivity.class);
                Uri contentURi = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                Log.i(CatalogActivity.class.getSimpleName(),"here URI:"+contentURi);
                open.setData(contentURi);
                startActivity(open);



            }
        });

        cursorAdapter = new PetCursorAdapter(this,null);

        displayView.setAdapter(cursorAdapter);

        View emptyView = findViewById(R.id.empty_view);

        displayView.setEmptyView(emptyView);

        getSupportLoaderManager().initLoader(0,null, this);

    }

    private void insertpet( ){
        petDbHelper mDbHelper = new petDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);
         getContentResolver().insert(PetEntry.CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertpet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        petDbHelper mDbHelper = new petDbHelper(this);

        // Create and/or open a database to read from it
        String[] projection ={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME ,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT

        } ;

//        Cursor cursor = db.query(PetEntry.TABLE_NAME, null,
//                null, null,
//                null, null, null);
        Cursor cursor = getContentResolver().query( PetEntry.CONTENT_URI , projection ,null , null , null );



            ListView displayView = (ListView) findViewById(R.id.List_view_pet);
            cursorAdapter = new PetCursorAdapter(this,cursor);
            displayView.setAdapter(cursorAdapter);

           View emptyView = findViewById(R.id.empty_view);
           displayView.setEmptyView(emptyView);




//            displayView.append( "\n"+PetEntry._ID + " - " +
//                    PetEntry.COLUMN_PET_NAME + " - " +PetEntry.COLUMN_PET_BREED+ " - " +PetEntry.COLUMN_PET_GENDER+ " - " +PetEntry.COLUMN_PET_WEIGHT+"\n" );

            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
//            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
//            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
//            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
//
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentbreed = cursor.getString(breedColumnIndex);
//                int currentgender = cursor.getInt(genderColumnIndex);
//                int currentweight = cursor.getInt(weightColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
//                displayView.append(("\n" + currentID + " - " +
//                        currentName+" - "+currentbreed+ " - " +currentgender+ " - " +currentweight));


                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
            }





    @Override
    public Loader<Cursor> onCreateLoader(int id,  Bundle args) {
        petDbHelper mDbHelper = new petDbHelper(this);

        // Create and/or open a database to read from it
        String[] projection ={
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME ,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT

        } ;

//        Cursor cursor = db.query(PetEntry.TABLE_NAME, null,
//                null, null,
//                null, null, null);



        return  new CursorLoader( this,PetEntry.CONTENT_URI , projection ,null , null , null );

    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }

}
