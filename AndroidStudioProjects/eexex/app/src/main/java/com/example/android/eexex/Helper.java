package com.example.android.eexex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Helper extends SQLiteOpenHelper {

    public Helper(Context context) {
        super(context, "quran.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + Contract.PetEntry.TABLE_NAME + " ("
                + Contract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.PetEntry.alsoura + " TEXT NOT NULL, "
                + Contract.PetEntry.alaya + " INTEGER , "
                + Contract.PetEntry.algza + " INTEGER , "
                + Contract.PetEntry.alsafha + " INTEGER );";

        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
