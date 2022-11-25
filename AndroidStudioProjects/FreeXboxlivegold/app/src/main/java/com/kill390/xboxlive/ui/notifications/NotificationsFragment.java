package com.kill390.xboxlive.ui.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kill390.xboxlive.AccountsActivity;
import com.kill390.xboxlive.DashboardActivity;
import com.kill390.xboxlive.MainActivity;
import com.kill390.xboxlive.R;
import com.kill390.xboxlive.services.NotificationService;

import java.util.Objects;

public class NotificationsFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    boolean isChecked0 = false ;

    Switch sw ;


    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        DashboardActivity.title.setText("Settings");

        Button contactBt = root.findViewById(R.id.contactSupport);
        Button signOutBt = root.findViewById(R.id.signout);
        Button accountsBt = root.findViewById(R.id.myPlusAccounts);
        TextView versionTextView = root.findViewById(R.id.versionTextView);

        try {
            PackageInfo pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
            if (pInfo != null) {
                String version = pInfo.versionName;
                versionTextView.setText("v" + version);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:osamatamer390@gmail.com"));// only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "Free Xbox live gold");
                startActivity(intent);
            }
        });

        accountsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...",
                        "Loading. Please wait...", true);

                final boolean[] isTrue = {false};

                FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dialog.dismiss();
                        if (!isTrue[0]) {
                            if (dataSnapshot.child("accounts").getValue() != null) {
                                Intent intent = new Intent(getActivity(), AccountsActivity.class);
                                startActivity(intent);
                                isTrue[0] = true;
                            } else {
                                Toast.makeText(getActivity(), "Please make sure you have buy an account", Toast.LENGTH_LONG).show();
                                isTrue[0] = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(requireContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        sw = root.findViewById(R.id.switch2);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.kill390.xboxlive", Context.MODE_PRIVATE);

        isChecked0 = sharedPreferences.getBoolean("isChecked",false);

        sw.setChecked(isChecked0);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isChecked0 = isChecked;

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.kill390.xboxlive", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("isChecked",isChecked).apply();

                if (isChecked){
                    Intent mServiceIntent = new Intent(getContext(), NotificationService.class);
                    if (!isMyServiceRunning(NotificationService.class)) {
                        getActivity().startService(mServiceIntent);
                    }
                }else {
                    Intent mServiceIntent = new Intent(getActivity(), NotificationService.class);
                    getActivity().stopService(mServiceIntent);


                }

                Log.i("ischecked", String.valueOf(isChecked));

            }
        });



        return root;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
}
