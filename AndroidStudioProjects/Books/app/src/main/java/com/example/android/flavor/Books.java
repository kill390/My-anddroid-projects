package com.example.android.flavor;


public class Books {

    private String mtitle;

    private String mdescreption;
    private String mImageurl;
    private String mUrl;


    public Books(String title, String descreption, String imageurl, String url) {
        mtitle = title;
        mdescreption = descreption;
        mImageurl = imageurl;
        mUrl = url;
    }


    public String getMtitle() {
        return mtitle;
    }


    public String getMdescreption() {
        return mdescreption;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public String getmUrl() {
        return mUrl;
    }


}
