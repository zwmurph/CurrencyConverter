package com.zacmurphy.currencyconverter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Currency loader task
 */
class CurrencyLoader extends AsyncTaskLoader<List<Currency>> {

    //Log tag that returns the package name for errors
    private static final String LOG_TAG = CurrencyLoader.class.getSimpleName();

    //Global instance of the String URL, so it can be used in multiple methods in this class
    private final String mUrl;

    /**
     * The constructor for this class
     */
    public CurrencyLoader(Context context) {
        super(context);
        mUrl = LoadingActivity.REQUEST_URL;
        Log.d(LOG_TAG, "constructor - called");
    }

    /**
     * When the loader is instructed to load
     */
    @Override
    protected void onStartLoading() {
        //Force the load
        forceLoad();
        Log.d(LOG_TAG, "onStartLoading - called");
    }

    /**
     * The background process for the loader
     */
    @Override
    public List<Currency> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground - called");
        //Don't perform the request if there are no URLs, or the URL is null
        if (mUrl == null) {
            Log.v(LOG_TAG, "mUrl is null");
            return null;
        }

        //Get the data for the URL provided & Return the result
        return QueryUtils.fetchCurrencyData(mUrl);
    }
}