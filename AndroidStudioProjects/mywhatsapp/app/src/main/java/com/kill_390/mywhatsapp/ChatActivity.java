package com.kill_390.mywhatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    EditText chatEd ;
    ImageButton imageButton ;
    ProgressBar progressBar ;
    ArrayList<ChatM> arrayList = new ArrayList<>();
    ChatAdabter adapter ;

    public void send(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String message = chatEd.getText().toString();
        ParseObject object = new ParseObject("chats");
        object.put("to",getIntent().getStringExtra("to"));
        object.put("from", ParseUser.getCurrentUser().getUsername());
        object.put("message",message);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                chatEd.setText("");
                if (e == null){
                    Log.i("here","saved");
                    search();
                }else{
                    Log.i("here",e.getMessage());
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

         progressBar = findViewById(R.id.progressBar);

        final ListView listView = findViewById(R.id.chatListView);
        listView.setEmptyView(progressBar);

        adapter = new ChatAdabter(this,arrayList);
        listView.setAdapter(adapter);

        search();

        chatEd = findViewById(R.id.chatEditText);
       imageButton = findViewById(R.id.imageButton);

       imageButton.setEnabled(false);

       chatEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view , boolean hasFocus) {
               if (!hasFocus){
                   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
               }
           }
       });

       chatEd.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (count>0){
                   imageButton.setEnabled(true);
               }else{
                   imageButton.setEnabled(false);
               }
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });

       new Timer().scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {
               search();
           }
       },10000,1000);
    }

    public void search(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("chats");

        query.whereEqualTo("to",ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("from",getIntent().getStringExtra("to"));


        ParseQuery<ParseObject> myMessages = ParseQuery.getQuery("chats");

        myMessages.whereEqualTo("to",getIntent().getStringExtra("to"));
        myMessages.whereEqualTo("from",ParseUser.getCurrentUser().getUsername());


        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        queries.add(query);
        queries.add(myMessages);

        ParseQuery<ParseObject> query1 = ParseQuery.or(queries);

        query1.orderByAscending("createdAt");

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null){
                    arrayList.clear();
                    if (objects.size()>0){
                        for (ParseObject object : objects){

                            String message = object.getString("message");
                            Date date = object.getUpdatedAt();

                            Log.i("here",message + date );

                            if (object.getString("from").equals(getIntent().getStringExtra("to"))){
                                arrayList.add(new ChatM(message , date,2));
                                adapter.notifyDataSetChanged();
                            }else {
                                arrayList.add(new ChatM(message, date, 1));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });

    }
}
