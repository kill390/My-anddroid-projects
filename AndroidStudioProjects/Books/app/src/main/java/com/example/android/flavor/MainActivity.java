
package com.example.android.flavor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private SearchView searchView;
    private Badapter adapter;
    private String searchinput;
    private String USGS_REQUEST_URL;
    private View loadingbar;
    private TextView textErrors;
    private ListView listView;
    private String maxResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        searchView = (SearchView) findViewById(R.id.search_viewr);
        listView = (ListView) findViewById(R.id.listview);
        loadingbar = (View) findViewById(R.id.loading_spinner);
        loadingbar.setVisibility(View.GONE);

        adapter = new Badapter(this, new ArrayList<Books>());

        listView.setAdapter(adapter);

        textErrors = (TextView) findViewById(R.id.empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Books currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri boookReviewUri = Uri.parse(currentEarthquake.getmUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, boookReviewUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.clear();

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                maxResults = sharedPrefs.getString(getString(R.string.list),"" );


                searchinput = query;
                USGS_REQUEST_URL =
                        "https://www.googleapis.com/books/v1/volumes?q=" + searchinput + "&maxResults="+maxResults;

                Log.i(LOG_TAG , "url is here: "+ USGS_REQUEST_URL);


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                listView.setEmptyView(loadingbar);

                textErrors.setVisibility(View.GONE);

                if (networkInfo != null && networkInfo.isConnected()) {

                    bookasynktask task = new bookasynktask();

                    task.execute(USGS_REQUEST_URL);


                } else {
                    loadingbar.setVisibility(View.GONE);

                    textErrors.setText(R.string.internet);

                    listView.setEmptyView(textErrors);
                }

                Log.i(LOG_TAG, "search result :" + searchinput);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });

    }


    private class bookasynktask extends AsyncTask<String, Void, ArrayList<Books>> {

        @Override
        protected ArrayList<Books> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            ArrayList<Books> earthquake = Network.fetch(USGS_REQUEST_URL);


            return earthquake;
        }

        @Override
        protected void onPostExecute(ArrayList<Books> data) {
            adapter.clear();
            loadingbar.setVisibility(View.GONE);

            if (data.isEmpty()) {
                textErrors.setText(R.string.nobooks);
                listView.setEmptyView(textErrors);

            }

            if (data != null && !data.isEmpty()) {


                adapter.addAll(data);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if(item.getItemId()==R.id.results){
            Intent set = new Intent(this,setting.class);
            startActivity(set);

        }

        return super.onOptionsItemSelected(item);
    }


}