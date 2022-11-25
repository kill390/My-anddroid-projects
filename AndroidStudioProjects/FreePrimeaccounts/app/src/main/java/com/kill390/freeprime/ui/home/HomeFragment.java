package com.kill390.freeprime.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kill390.freeprime.DashboardActivity;
import com.kill390.freeprime.R;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class HomeFragment extends Fragment {

    boolean rated;
    RewardedAd rewardedAd1;
    boolean rate_card = false;
    MaterialCardView inviteCard;
    MaterialCardView watchCard;
    MaterialCardView randomCard;
    MaterialCardView dailyCard;
    MaterialCardView rateCard;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    InterstitialAd mInterstitialAd;
    boolean isShowin = false;
    String ID_REWARD_AD = "ca-app-pub-2000845409770635/1254017559";
    String ID_INTERSTITIAL_AD = "ca-app-pub-2000845409770635/3197832222";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        DashboardActivity.title.setText("Offers");

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mInterstitialAd = createAd();

        rewardedAd1 = createAndLoadRewardedAd(ID_REWARD_AD);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inviteCard = root.findViewById(R.id.inviteCard);
        watchCard = root.findViewById(R.id.watchCard);
        randomCard = root.findViewById(R.id.randomCard);
        dailyCard = root.findViewById(R.id.dailyCard);
        rateCard = root.findViewById(R.id.rate_card);

        FirebaseDatabase.getInstance().getReference().child("rate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("true")) {
                    rate_card = true;
                    if (rated ) {
                        rateCard.setVisibility(View.GONE);
                    } else {
                        rateCard.setVisibility(View.VISIBLE);
                    }
                } else {
                    rate_card = false;
                    rateCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
        rated = sharedPreferences.getBoolean("rated", false);
        if (rated && !rate_card) {
            rateCard.setVisibility(View.GONE);
        } else if (rate_card && !rated) {
            rateCard.setVisibility(View.VISIBLE);
        }


        inviteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String invitecode = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                new AlertDialog.Builder(requireContext()).setTitle("Invite a friend")
                        .setMessage("this is your invite code:\n" + invitecode + "\n your friend will get 30 coins")
                        .setIcon(android.R.drawable.ic_menu_share)
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "https://play.google.com/store/apps/details?id=com.kill390.freeprime" + "\nthis is your invite code: " + invitecode;
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                    mInterstitialAd = createAd();
                                }
                            }
                        })
                        .show();
            }
        });

        watchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading Ad",
                        "Loading Ad. Please wait...", true);
                final Activity activityContext = getActivity();

                final RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.
                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                        rewardedAd1 = createAndLoadRewardedAd(ID_REWARD_AD);
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {

                        rewardedAd1 = createAndLoadRewardedAd(ID_REWARD_AD);
                        final Boolean[] checked = {true};
                        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (checked[0]) {
                                    checked[0] = false;
                                    //inviter user
                                    String coins = Objects.requireNonNull(dataSnapshot.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("coins").getValue()).toString();
                                    int coins2 = Integer.parseInt(coins);

                                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("coins")
                                            .setValue(coins2 + 15);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int errorCode) {
                        // Ad failed to display.
                        rewardedAd1 = createAndLoadRewardedAd(ID_REWARD_AD);
                    }

                };

                if (rewardedAd1.isLoaded()) {
                    dialog.dismiss();
                    rewardedAd1.show(activityContext, adCallback);
                }


                new CountDownTimer(3500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            if (rewardedAd1.isLoaded()) {
                                rewardedAd1.show(activityContext, adCallback);
                            } else {
                                Snackbar.make(v, "Failed to load Ad please try again later", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark)).show();
                            }
                        }
                    }
                }.start();
            }
        });
        randomCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Settings.Global.getInt(requireActivity().getContentResolver(), Settings.Global.AUTO_TIME) == 1) {

                        SharedPreferences sp = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
                        String savedDateTime = sp.getString("random", "");
                        if ("".equals(savedDateTime)) {
                            random();
                        } else {
                            String dateStringNow = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
                            //compare savedDateTime with today's datetime (dateStringNow), and act accordingly
                            if (savedDateTime.equals(dateStringNow)) {
                                //same date; disable button
                                new AlertDialog.Builder(requireContext()).setTitle("Sorry:(")
                                        .setMessage("this is allowed once a day come tomorrow and get it")
                                        .setIcon(android.R.drawable.stat_notify_error)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (mInterstitialAd.isLoaded()) {
                                                    mInterstitialAd.show();
                                                    mInterstitialAd = createAd();
                                                } else {
                                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                                }
                                            }
                                        })
                                        .show();
                            } else {
                                //different date; allow button click
                                random();
                            }
                        }
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setIcon(android.R.drawable.stat_notify_error)
                                .setTitle("Error")
                                .setMessage("please turn on auto Time and try again")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                        requireActivity().finish();
                                    }
                                }).create().show();
                    }

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });
        dailyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Settings.Global.getInt(requireActivity().getContentResolver(), Settings.Global.AUTO_TIME) == 1) {

                        SharedPreferences sp = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
                        String savedDateTime = sp.getString("daily", "");
                        if ("".equals(savedDateTime)) {
                            daily();
                        } else {
                            String dateStringNow = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
                            //compare savedDateTime with today's datetime (dateStringNow), and act accordingly
                            if (savedDateTime.equals(dateStringNow)) {
                                //same date; disable button
                                new AlertDialog.Builder(requireContext()).setTitle("Sorry:(")
                                        .setMessage("this is allowed once a day come tomorrow and get it")
                                        .setIcon(android.R.drawable.stat_notify_error)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (mInterstitialAd.isLoaded()) {
                                                    mInterstitialAd.show();
                                                    mInterstitialAd = createAd();
                                                } else {
                                                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                                                }
                                            }
                                        })
                                        .show();
                            } else {
                                //different date; allow button click
                                daily();
                            }
                        }
                    } else {
                        new AlertDialog.Builder(requireContext())
                                .setIcon(android.R.drawable.stat_notify_error)
                                .setTitle("Error")
                                .setMessage("please turn on auto Time and try again")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                                        requireActivity().finish();
                                    }
                                }).create().show();
                    }

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        rateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("rated", true).apply();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kill390.freeprime"));
                startActivity(Intent.createChooser(intent, "open via"));

                final Boolean[] checked = {true};
                FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (checked[0]) {
                            checked[0] = false;

                            String st = Objects.requireNonNull(dataSnapshot.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("coins").getValue()).toString();
                            int coins2 = Integer.parseInt(st);

                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("coins")
                                    .setValue(coins2 + 30);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                rateCard.setVisibility(View.GONE);
            }
        });

        return root;
    }

    public void random() {
        final String dateString = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
        SharedPreferences sp = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("random", dateString);
        editor.apply();

        Random random = new Random();
        final int coins = random.nextInt(149) + 1;
        final Boolean[] checked = {true};
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checked[0]) {
                    checked[0] = false;

                    String st = Objects.requireNonNull(dataSnapshot.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("coins").getValue()).toString();
                    int coins2 = Integer.parseInt(st);

                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("coins")
                            .setValue(coins2 + coins);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(requireContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
        new AlertDialog.Builder(requireContext()).setTitle("Congratulations :)")
                .setMessage("you have earned " + coins + " coins (:")
                .setIcon(R.drawable.gift)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            mInterstitialAd = createAd();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }
                })
                .show();
    }

    public void daily() {
        String dateString = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
        SharedPreferences sp = requireContext().getSharedPreferences("com.kill390.freeprime.ui.home", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("daily", dateString);
        editor.apply();


        final Boolean[] checked = {true};
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checked[0]) {
                    checked[0] = false;
                    //inviter user
                    String st = Objects.requireNonNull(dataSnapshot.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("coins").getValue()).toString();
                    int coins2 = Integer.parseInt(st);

                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("coins")
                            .setValue(coins2 + 50);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        new AlertDialog.Builder(requireContext()).setTitle("Congratulations :)")
                .setMessage("you have earned 50 coins (:")
                .setIcon(R.drawable.gift)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            mInterstitialAd = createAd();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }
                })
                .show();
    }

    public RewardedAd createAndLoadRewardedAd(String adUnitId) {
        RewardedAd rewardedAd = new RewardedAd(requireContext(), adUnitId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                watchCard.setEnabled(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.

            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    public InterstitialAd createAd() {
        InterstitialAd mInterstitialAd = new InterstitialAd(requireContext());
        mInterstitialAd.setAdUnitId(ID_INTERSTITIAL_AD);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        return mInterstitialAd;
    }

}
