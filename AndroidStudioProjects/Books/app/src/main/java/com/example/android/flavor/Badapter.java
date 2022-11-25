package com.example.android.flavor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Badapter extends ArrayAdapter<Books> {

    private static final String LOG_TAG = Badapter.class.getSimpleName();


    public Badapter(Activity context, ArrayList<Books> androidFlavors) {

        super(context, 0, androidFlavors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Books currentBooks = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);

        title.setText(currentBooks.getMtitle());

        TextView descreption = (TextView) listItemView.findViewById(R.id.descreption);

        descreption.setText(currentBooks.getMdescreption());


        ImageView tumbnail = (ImageView) listItemView.findViewById(R.id.image);
        Picasso.get().load(currentBooks.getmImageurl()).into(tumbnail);


        return listItemView;
    }

}
