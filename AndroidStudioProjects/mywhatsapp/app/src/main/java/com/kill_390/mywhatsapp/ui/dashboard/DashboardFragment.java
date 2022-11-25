package com.kill_390.mywhatsapp.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kill_390.mywhatsapp.ChatActivity;
import com.kill_390.mywhatsapp.R;
import com.kill_390.mywhatsapp.ui.home.HomeFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter<String> adapter ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final ProgressBar progressBar = root.findViewById(R.id.progressBar2);
        ListView listView = root.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,users);
        listView.setAdapter(adapter);
        listView.setEmptyView(progressBar);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                progressBar.setVisibility(View.INVISIBLE);
                if (e == null && objects.size()>0){
                    for (ParseUser object:objects){
                        users.add(object.getUsername());
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("to",users.get(position));
                startActivity(intent);

                HomeFragment.chats2.add(users.get(position));

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.kill_390.mywhatsapp", Context.MODE_PRIVATE);
                sharedPreferences.edit().putStringSet("chats", HomeFragment.chats2).apply();

                HomeFragment.adapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}
