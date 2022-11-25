package com.kill390.xboxlive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class InviteActivity extends AppCompatActivity {

    TextInputEditText inviteEditText;
    Boolean checked = true;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        inviteEditText = findViewById(R.id.inviteEditText);
    }

    public void skip(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void check(View view) {
        final ProgressDialog dialog = ProgressDialog.show(InviteActivity.this, "Checking",
                "Checking. Please wait...", true);
        final String inviteCode = Objects.requireNonNull(inviteEditText.getText()).toString().trim();
        if (inviteCode.length() > 0) {
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (checked) {
                        if (dataSnapshot.child(inviteCode).getValue() != null) {
                            checked = false;
                            final Intent intent = new Intent(InviteActivity.this, DashboardActivity.class);
                            //inviter user
                            String coins = dataSnapshot.child(inviteCode).child("coins").getValue().toString();
                            int coins2 = Integer.parseInt(coins);

                            FirebaseDatabase.getInstance().getReference().child("users").child(inviteCode).child("coins")
                                    .setValue(coins2 + 100).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //current user
                                    if (task.isSuccessful()) {

                                        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("coins")
                                                .setValue(30);

                                        dialog.dismiss();

                                        startActivity(intent);
                                        finish();

                                    } else if (task.isCanceled()) {
                                        Toast.makeText(InviteActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            dialog.dismiss();
                            inviteEditText.setError("Invalid Token");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Snackbar.make(view, "please inter your code", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();
        }
    }
}
