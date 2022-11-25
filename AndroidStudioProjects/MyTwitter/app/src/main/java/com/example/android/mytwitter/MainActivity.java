package com.example.android.mytwitter;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText username ;
    EditText password ;

    public void loged(){
        if (ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void sginupLogin(View view){
        final String usernameText = username.getText().toString();
        final String passwordText = password.getText().toString();
        if (usernameText.length()>0 && passwordText.length()>0) {
            ParseUser.logInInBackground(usernameText, passwordText, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        loged();
                        Toast.makeText(MainActivity.this, "loged in ok !!", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseUser newuser = new ParseUser();
                        newuser.setUsername(usernameText);
                        newuser.setPassword(passwordText);
                        newuser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    loged();
                                    Toast.makeText(MainActivity.this, "signup ok!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this, "please check your information", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loged();
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
    }
}
