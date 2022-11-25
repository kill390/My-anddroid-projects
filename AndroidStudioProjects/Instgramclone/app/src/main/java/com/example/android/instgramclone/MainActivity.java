package com.example.android.instgramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity  {

    public void log(View view){
        if (username.getText().length()>0||password.getText().length()>0) {

            if (isLogin) {


                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(MainActivity.this , LogoutActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });

            }
            if (isSignup) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(MainActivity.this , LogoutActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });


            }
        }else{
            Toast.makeText(this, "please check your informaion", Toast.LENGTH_SHORT).show();
        }

    }

    EditText username ;
    EditText password ;
    TextView change ;
    Button button ;
    Boolean isSignup = true;
    Boolean isLogin = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this,LogoutActivity.class);
            startActivity(intent);
        }

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        change=findViewById(R.id.change);
        button=findViewById(R.id.button);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideKeyboard(v);
                }
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    log(v);
                }
                return true;
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignup){
                    isLogin=true;
                    isSignup=false;
                    change.setText("or, Signup");
                    button.setText("Login");
                } else if (isLogin){
                    isLogin=false;
                    isSignup=true;
                    change.setText("or,login");
                    button.setText("Signup");
                }
                Log.i("here","isclicked0");
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
