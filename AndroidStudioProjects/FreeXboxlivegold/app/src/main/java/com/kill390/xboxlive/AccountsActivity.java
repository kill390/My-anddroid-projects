package com.kill390.xboxlive;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountsActivity extends AppCompatActivity {

    TextView waitText;
    TextView emailText;
    TextView passwordText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        waitText = findViewById(R.id.textView7);
        emailText = findViewById(R.id.emailTextView);
        passwordText = findViewById(R.id.passwordTextView);

        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Objects.requireNonNull(dataSnapshot.child("accounts").child("email").getValue()).toString().equals("null")) {
                    waitText.setVisibility(View.VISIBLE);
                } else {
                    waitText.setVisibility(View.GONE);
                    emailText.setText(Objects.requireNonNull(dataSnapshot.child("accounts").child("email").getValue()).toString());
                    passwordText.setText(Objects.requireNonNull(dataSnapshot.child("accounts").child("password").getValue()).toString());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AccountsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
