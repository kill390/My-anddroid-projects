package com.example.android.quakereport;

import android.graphics.Color;

public class Earth {
    private double mVersionName;
    private String osama2;
    private long mTimeInMilliseconds ;
    private String mUrl;



    public  Earth ( double Name, String mName , long timeInMilliseconds,String mUrll ) {

        mVersionName = Name;
        osama2 = mName;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = mUrll;

    }
    /**
     * Get the name of the Android version
     */
    public String getmUrl() {
        return mUrl;
    }

    public double getVersionName() {
        return mVersionName;
    }

    /**
     * Get the Android version number
     //     */
    public String getOsama2() {
        return osama2;
    }


    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds ;
    }


}




