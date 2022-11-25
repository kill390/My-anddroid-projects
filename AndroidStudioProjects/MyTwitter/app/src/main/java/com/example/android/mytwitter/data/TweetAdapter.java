package com.example.android.mytwitter.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.mytwitter.R;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends ArrayAdapter<StTweet> {

    public TweetAdapter(Context context,  ArrayList<StTweet> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        StTweet currentStTweet = getItem(position);

        TextView tweetText = listItemView.findViewById(R.id.tweetText);
        tweetText.setText(currentStTweet.getmTweet());


        TextView userText = listItemView.findViewById(R.id.userText);
        userText.setText(currentStTweet.getmUser());


        return listItemView ;
    }
}
