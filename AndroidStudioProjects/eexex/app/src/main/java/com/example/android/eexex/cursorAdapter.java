package com.example.android.eexex;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class cursorAdapter extends CursorAdapter {

    public cursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.activity_fww2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.edit);
        TextView summaryTextView = (TextView) view.findViewById(R.id.edit2);
        TextView algz = (TextView) view.findViewById(R.id.edit3);
        TextView alsafhatext = (TextView) view.findViewById(R.id.edit4);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(Contract.PetEntry.alsoura);
        int breedColumnIndex = cursor.getColumnIndex(Contract.PetEntry.alaya);
        int algzColumnIndex = cursor.getColumnIndex(Contract.PetEntry.algza);
        int alsafaha = cursor.getColumnIndex(Contract.PetEntry.alsafha);
        // Read the pet attributes from the Cursor for the current pet
        String petName = cursor.getString(nameColumnIndex);
        int petBreed = cursor.getInt(breedColumnIndex);
        int algznumber = cursor.getInt(algzColumnIndex);
        int alsafhanumber = cursor.getInt(alsafaha);


        // Update the TextViews with the attributes for the current pet

        nameTextView.setText(petName);
        summaryTextView.setText(String.valueOf(petBreed));
        algz.setText(String.valueOf(algznumber));
        alsafhatext.setText(String.valueOf(alsafaha));






    }
}
