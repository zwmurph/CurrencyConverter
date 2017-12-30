package com.zacmurphy.currencyconverter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.zacmurphy.currencyconverter.Currency.currenciesList;

public class LoadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Currency>> {

    //Tag for the log messages
    private static final String LOG_TAG = LoadingActivity.class.getSimpleName();

    //URL that retrieves the JSON response from the API
    public static final String REQUEST_URL = "https://api.fixer.io/latest?symbols=USD,GBP";
    //https://api.fixer.io/latest?base=GBP
    //https://api.fixer.io/latest?symbols=USD,GBP

    //Global instance of the ProgressBar, so it can be used in multiple methods in this class
    private ProgressBar mProgressBar;

    //Global instance of the information TextView
    private TextView mInfoView;

    //Constant value for the currency loader ID. This is only really used if you're using multiple loaders.
    private static final int CURRENCY_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "LoadingActivity - started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Initialise the global variables
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mInfoView = (TextView) findViewById(R.id.info_view);

        //Check if the user is connected to the Internet
        if (!isInternetConnected()) {
            Log.v(LOG_TAG, "No Internet connection");
            //Hide the loading spinner
            mProgressBar.setVisibility(View.GONE);

            //Let the user know there is no connection
            mInfoView.setText(getResources().getText(R.string.error_noConnectivity));
        } else {
            Log.v(LOG_TAG, "Internet connection established");

            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            //because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CURRENCY_LOADER_ID, null, this);
        }
    }

    /**
     * Method that checks if the user is connected to the Internet
     *
     * @return true or false
     */
    private boolean isInternetConnected() {
        Log.d(LOG_TAG, "isInternetConnected() - called");
        //Create a connectivity manager
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get the active network's network info, and return a boolean value
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Method for the creation of the loader, pass in:
     * The Loader {@param id} to be used
     * The {@param args} (arguments) the Loader should take
     *
     * @return a new instance of the Loader
     */
    @Override
    public Loader<List<Currency>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader() - called");
        return new CurrencyLoader(this);
    }

    /**
     * Once the loader has finished, execute this method with:
     * The {@param loader} to be used
     * The {@param result} from the Loader creation
     */
    @Override
    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> currencies) {
        Log.d(LOG_TAG, "onLoadFinished() - called");
        //If there is a valid list of Currency's, then add them to the ArrayList
        if (currencies != null && !currencies.isEmpty()) {
            currenciesList.addAll(currencies);
            Log.v(LOG_TAG, "currencies added to ArrayList");

            //Re-order the ArrayList, to prioritise the more frequently used currencies to the beginning
            Collections.sort(currenciesList, new CurrencyComparator());
            Log.v(LOG_TAG, "currenciesList re-ordered");
        }

        //Remove the loading spinner and change the on-screen text
        mProgressBar.setVisibility(View.GONE);
        Log.d(LOG_TAG, "Error occurred?: " + QueryUtils.ERROR_OCCURRED);
        if (QueryUtils.ERROR_OCCURRED) {
            mInfoView.setText(getResources().getText(R.string.error_dataLoading));
        } else {
            mInfoView.setText(getResources().getText(R.string.message_dataLoaded));
            //Switch to the next activity
            switchActivity();
        }
    }

    /**
     * Custom method that switches the user to the next activity and closes the main
     */
    private void switchActivity() {
        Log.d(LOG_TAG, "Activity switching");
        //Create a new Intent object constructor, populate it with MainActivity.class
        Intent intent = new Intent(this, MainActivity.class);
        //Make sure the user cannot navigate back to this activity by using the back button
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Start that new intent
        startActivity(intent);
        //Close the current activity
        finish();
    }

    /**
     * Sub-class that sorts the ArrayList into the order specified by the item's priority,
     * when the items have the same priority, they are left in alphabetical order by country code
     */
    private class CurrencyComparator implements Comparator<Currency> {
        public int compare(Currency left, Currency right){
            Log.d(LOG_TAG, "compare class - called");
            return Integer.compare(left.getPriority(), right.getPriority());
        }
    }

    /**
     * If the Loader is reset (i.e. through orientation change), handle that in this method
     * The {@param loader} to be used
     */
    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {
        Log.d(LOG_TAG, "onLoaderReset() - called");
    }
}
