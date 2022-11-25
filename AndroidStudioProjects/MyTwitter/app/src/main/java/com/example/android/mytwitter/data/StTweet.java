package com.example.android.mytwitter.data;

public class StTweet {
    String mTweet ;
    String mUser ;

    public StTweet(String tweet , String user ){
        mTweet = tweet ;
        mUser = user ;
    }

    public String getmTweet() {
        return mTweet;
    }

    public String getmUser() {
        return mUser;
    }
}
