package com.kill390.freeprime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextInputEditText emailEditText;
    TextInputEditText passwordEditText;
    TextInputEditText nameEditText;
    String email;
    String password;
    String name;
    Button signupBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        signupBt = findViewById(R.id.signupButton);

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
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    signup(v);
                }
                return false;
            }
        });


    }

    public void signup(final View view) {
        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.kill390.freeprime", MODE_PRIVATE);
        hideKeyboard(view);
        boolean signedup = sharedPreferences.getBoolean("signedup", false);
        if (!signedup) {
            if (isNetworkAvailable()) {
                signupBt.setEnabled(false);
                final ProgressDialog dialog = ProgressDialog.show(SignupActivity.this, "Signing Up",
                        "Signing Up. Please wait...", true);

                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                name = nameEditText.getText().toString().trim();

                if (email.length() > 0 && password.length() > 0 && name.length() > 0) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    signupBt.setEnabled(true);
                                    dialog.dismiss();
                                    if (task.isSuccessful()) {
                                        sharedPreferences.edit().putBoolean("signedup", true).apply();
                                        Intent intent = new Intent(SignupActivity.this, InviteActivity.class);
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("password").setValue(password);
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(task.getResult().getUser().getEmail());
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("coins").setValue(0);
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("accounts").setValue(null);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Snackbar.make(view, task.getException().getMessage(),
                                                Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();

                                    }


                                }
                            });
                } else {
                    signupBt.setEnabled(true);
                    dialog.dismiss();
                    nameEditText.setError("");
                    emailEditText.setError("");
                    passwordEditText.setError("");
                    Snackbar.make(view, "please inter your information", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();
                }
            } else {
                Snackbar.make(view, "please check your internet", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();

            }
        } else {
            Toast.makeText(this, "you can make only one account in each device", Toast.LENGTH_LONG).show();
        }
    }

    public void loginNow(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
