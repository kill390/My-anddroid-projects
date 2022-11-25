package com.kill390.xboxlive.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kill390.xboxlive.DashboardActivity;
import com.kill390.xboxlive.R;

import java.util.Date;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    //    BillingProcessor bp;
    MaterialCardView coinBuyCard;
    //    MaterialCardView buyCard;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    private BillingClient billingClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        DashboardActivity.title.setText("Shop");



        coinBuyCard = root.findViewById(R.id.coinBuyCard);

        coinBuyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure ?")
                        .setMessage("are you sure you want to buy this for 750 coins")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int coins = Integer.parseInt(DashboardActivity.coins.getText().toString());
                                if (coins >= 1000) {
                                    coins = coins - 1000;
                                    FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                            .child("coins").setValue(coins).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("winners").child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            new AlertDialog.Builder(requireContext()).setTitle("Thank you for buying :)")
                                                                    .setMessage("to see your account go to settings click on \"My Xbox live accounts\"\nthe account will add to your app please wait.\n it may take between 1 hour and 48 hours")
                                                                    .setIcon(R.drawable.gift)
                                                                    .setPositiveButton("Ok", null)
                                                                    .show();
                                                        }
                                                    }
                                                });
                                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("accounts").child("email").setValue("null");
                                                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("accounts").child("password").setValue("null");

                                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.kill390.xboxlive", Context.MODE_PRIVATE);
                                                String dateStringNow = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
                                                sharedPreferences.edit().putString("buydate",dateStringNow).apply();
                                            } else {
                                                Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else if (coins < 1000) {
                                    new AlertDialog.Builder(requireContext()).setTitle("Sorry:(")
                                            .setMessage("you don't have enough money ")
                                            .setIcon(android.R.drawable.stat_notify_error)
                                            .setPositiveButton("Ok", null)
                                            .show();
                                }

                            }
                        })
                        .setNegativeButton("cancel", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

//        buyCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Are you sure ?")
//                        .setMessage("are you sure you want to buy this for 1.99$ coins")
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                bp.consumePurchase("android.test.purchased");
//                            }
//                        })
//                        .setNegativeButton("cancel",null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });

        return root;
    }

//    @Override
//    public void onProductPurchased(String productId, TransactionDetails details) {
//        FirebaseDatabase.getInstance().getReference().child("winners").child("spacial").child("email").setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    new AlertDialog.Builder(getContext()).setTitle("Thank you for buying :)")
//                            .setMessage("the account will be send to your email in 1 days.\nif the account wasn't sent in 1 days please contact our support.")
//                            .setIcon(R.drawable.gift)
//                            .setPositiveButton("Ok", null)
//                            .show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onPurchaseHistoryRestored() {
//
//    }
//
//    @Override
//    public void onBillingError(int errorCode, Throwable error) {
//
//        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onBillingInitialized() {
//
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}
