package com.kill_390.mywhatsapp.ui.home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kill_390.mywhatsapp.ChatActivity;
import com.kill_390.mywhatsapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static ArrayList<String> chats = new ArrayList<>();
    public static Set<String> chats2 = new HashSet<>();
    public static ArrayAdapter<String> adapter ;
     SharedPreferences sharedPreferences ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

         sharedPreferences = getContext().getSharedPreferences("com.kill_390.mywhatsapp", Context.MODE_PRIVATE);


        chats2 = new HashSet<>(sharedPreferences.getStringSet("chats", chats2));

            if (chats2!=null) {
                chats = new ArrayList<String>(chats2);
            }

        ListView listView = root.findViewById(R.id.homeListView);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,chats);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertdesplay("Delete chat","Are you sure?!",position);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("to",chats.get(position));
                startActivity(intent);
            }
        });


        return root;
    }
    public void alertdesplay(String title , String message, final int position){
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setTitle(title)
                .setIcon(android.R.drawable.stat_sys_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chats2.remove(chats.get(position));
                        chats.remove(position);
                        sharedPreferences.edit().remove("chats").apply();
                        sharedPreferences.edit().putStringSet("chats",chats2).apply();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

}
