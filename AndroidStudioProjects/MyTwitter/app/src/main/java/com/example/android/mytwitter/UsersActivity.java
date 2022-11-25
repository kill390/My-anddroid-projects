package com.example.android.mytwitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ArrayList<String> array = new ArrayList<>();
    ArrayAdapter<String> adapter ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mennu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                ParseUser.logOut();

                Intent intent = new Intent(UsersActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            case R.id.tweet:
                final EditText editText = new EditText(this);

                new AlertDialog.Builder(this)

                        .setTitle("share a Tweet")

                        .setView(editText)

                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.i("here", editText.getText().toString());

                                ParseObject object = new ParseObject("Tweets");

                                object.put("tweet", editText.getText().toString());

                                object.put("username", ParseUser.getCurrentUser().getUsername());

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (e == null) {

                                            Toast.makeText(UsersActivity.this, "Tweet Shared!", Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(UsersActivity.this, "Tweet Failed :(", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("cancel", null)
                        .show();

            case R.id.tweetFeed:

                Intent intent2 = new Intent(UsersActivity.this, FeedActivity.class);
                startActivity(intent2);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        final ListView listView = findViewById(R.id.listView);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if ( e == null && objects.size()>0){
                    for (ParseObject object : objects){
                        array.add(object.getString("username"));
                        adapter.notifyDataSetChanged();
                    }

                    for (String users : array){
                        if (ParseUser.getCurrentUser().getList("isFollowing").contains(users)) {
                            listView.setItemChecked(array.indexOf(users),true);
                        }
                    }
                }
            }
        });



        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_checked,array);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()){
                    ParseUser.getCurrentUser().add("isFollowing",array.get(position));
                }else{
                    List list = ParseUser.getCurrentUser().getList("isFollowing") ;
                    list.remove(array.get(position));
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing",list);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
    }
}
