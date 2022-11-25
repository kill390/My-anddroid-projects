package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



    public class petDbHelper extends SQLiteOpenHelper {
        public static final String LOG_TAG = petDbHelper.class.getSimpleName();


        public static final String DATABASE_NAME = "shelter.db";


        private static final int DATABASE_VERSION = 1;

        public petDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + cotract.PetEntry.TABLE_NAME + " ("
                    + cotract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + cotract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                    + cotract.PetEntry.COLUMN_PET_BREED + " TEXT , "
                    + cotract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                    + cotract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

            db.execSQL(SQL_CREATE_PETS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


