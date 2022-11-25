package com.kill_390.mywhatsapp;

import java.util.Date;

public class ChatM {
    String mMessage ;
    Date mDate ;
    int mM ;

    public ChatM(String message , Date date , int m ){
        mMessage = message ;
        mDate = date ;
        mM = m ;
    }

    public Date getmDate() {
        return mDate;
    }

    public String getmMessage() {
        return mMessage;
    }

    public int getmM() {
        return mM;
    }
}
