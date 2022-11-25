package com.kill390.xboxlive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    String email;
    String password;
    Button signinBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signinBt = findViewById(R.id.loginButton);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    login(v);
                }
                return false;
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if (isNetworkAvailable()) {
            if (mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(final View view) {
        hideKeyboard(view);
        if (isNetworkAvailable()) {
            signinBt.setEnabled(false);
            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "LogingIn",
                    "LogingIn. Please wait...", true);
            email = Objects.requireNonNull(emailEditText.getText()).toString();
            password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if (email.length() > 0 && password.length() > 0) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signinBt.setEnabled(true);
                                dialog.dismiss();
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Snackbar.make(view, Objects.requireNonNull(task.getException()).getMessage(),
                                            Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();
                                }
                            }
                        });
            } else {
                signinBt.setEnabled(true);
                dialog.dismiss();
                emailEditText.setError("");
                passwordEditText.setError("");
                Snackbar.make(view, "please inter your information", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();
            }
        } else {
            Snackbar.make(view, "please check you internet", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();

        }
    }

    public void signupNow(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
