package com.example.android.mytwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.android.mytwitter.data.StTweet;
import com.example.android.mytwitter.data.TweetAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ArrayList<StTweet> stTweetArrayList = new ArrayList<StTweet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ListView listView = findViewById(R.id.tweetListView);

        final TweetAdapter adapter = new TweetAdapter(this,stTweetArrayList);

        listView.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweets");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0){
                    for (ParseObject object : objects){
                        stTweetArrayList.add(new StTweet(object.getString("tweet"),object.getString("username")));
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });

    }
}
