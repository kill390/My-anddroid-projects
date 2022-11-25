package com.example.android.quakereport;
import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class LoaderE extends AsyncTaskLoader<List<Earth>> {

    /** Tag for log messages */
    private static final String LOG_TAG = LoaderE.class.getName();

    /** Query URL */
    private String mUrl;


    public LoaderE(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Earth> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Earth> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}

