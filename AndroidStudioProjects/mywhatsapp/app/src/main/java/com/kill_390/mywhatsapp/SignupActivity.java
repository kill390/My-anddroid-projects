package com.kill_390.mywhatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText usernameEditText ;
    TextInputEditText passwordEditText ;
    Button button ;

    public void log(final View view){
        button.setEnabled(false);
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (usernameEditText.getText().length()>0&&passwordEditText.getText().length()>0){
            // login if not signup.
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null){
                        button.setEnabled(true);
                        success();
                    }else{
                        //if there error signup .
                        ParseUser newuser = new ParseUser();
                        newuser.setUsername(username);
                        newuser.setPassword(password);
                        newuser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    button.setEnabled(true);
                                    success();
                                }else{
                                    button.setEnabled(true);
                                    Snackbar.make(view,e.getMessage(),Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_light)).show();
                                }
                            }
                        });

                    }
                }
            });

        }else{
            Snackbar.make(view,"Please fill your information",Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_light)).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (ParseUser.getCurrentUser()!=null){
            success();
        }

        usernameEditText = findViewById(R.id.usernameEdit);
        passwordEditText = findViewById(R.id.passwordEdit);
        button = findViewById(R.id.button);

    }
    public void success(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
