package com.kill_390.mywhatsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatAdabter extends ArrayAdapter<ChatM> {
    public ChatAdabter(@NonNull Context context, @NonNull ArrayList<ChatM> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);

        }
        ChatM currentChat = getItem(position);

        if (currentChat.getmM() == 2) {

            TextView messageText = listItemView.findViewById(R.id.messageTextView);
            messageText.setText(currentChat.getmMessage());
            TextView dateText = listItemView.findViewById(R.id.timeTextView);
            String[] date = currentChat.getmDate().toString().split(" G");
            dateText.setText(date[0]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            messageText.setLayoutParams(params);
            dateText.setLayoutParams(params);

            GradientDrawable magnitudeCircle = (GradientDrawable) messageText.getBackground();
            magnitudeCircle.setColor(listItemView.getResources().getColor(android.R.color.holo_green_light));
        } else {

            TextView messageText = listItemView.findViewById(R.id.messageTextView);
            messageText.setText(currentChat.getmMessage());

            TextView dateText = listItemView.findViewById(R.id.timeTextView);
            String[] date = currentChat.getmDate().toString().split(" G");
            dateText.setText(date[0]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            messageText.setLayoutParams(params);
            dateText.setLayoutParams(params);

            GradientDrawable magnitudeCircle = (GradientDrawable) messageText.getBackground();
            magnitudeCircle.setColor(listItemView.getResources().getColor(android.R.color.holo_orange_dark));
        }
        return listItemView;
    }
}
