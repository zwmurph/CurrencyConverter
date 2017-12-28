package com.zacmurphy.currencyconverter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.zacmurphy.currencyconverter.Currency.currenciesList;

public class LoadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Currency>> {

    //Tag for the log messages
    private static final String LOG_TAG = LoadingActivity.class.getSimpleName();

    //URL that retrieves the JSON response from the API
    public static final String REQUEST_URL = "https://api.fixer.io/latest?base=GBP";
    //https://api.fixer.io/latest?base=GBP

    //Global instance of the ProgressBar, so it can be used in multiple methods in this class
    private ProgressBar mProgressBar;

    //Global instance of the information TextView
    private TextView mInfoView;

    //Constant value for the currency loader ID. This is only really used if you're using multiple loaders.
    private static final int CURRENCY_LOADER_ID = 1;

    //Global instance of the CurrencyAdapter, so it can be used in multiple methods in this class
    private CurrencyAdapter mAdapter;

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
            //Creates an adapter for the words to use, appends the array of words to the adapter,
            //the adapter is responsible for making a View for each item in the data set
            mAdapter = new CurrencyAdapter(this, new ArrayList<Currency>());

            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            //the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            //because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CURRENCY_LOADER_ID, null, this);
        }
        //mProgressBar.setVisibility(View.GONE);
        //mInfoView.setText(getResources().getText(R.string.message_dataLoaded));
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
        //If there is a valid list of Currency's, then add them to the adapter's dataset
        //This triggers the ListView to update
        if (currencies != null && !currencies.isEmpty()) {
            mAdapter.addAll(currencies);
            currenciesList.addAll(currencies);
            Log.v(LOG_TAG, "currencies added to adapter");
        }

        //Remove the loading spinner and change the on-screen text
        mProgressBar.setVisibility(View.GONE);
        Log.d(LOG_TAG, "Error occurred?: " + QueryUtils.ERROR_OCCURRED);
        if (QueryUtils.ERROR_OCCURRED) {
            mInfoView.setText(getResources().getText(R.string.error_dataLoading));
        } else {
            mInfoView.setText(getResources().getText(R.string.message_dataLoaded));
        }

        //Switch to the next activity
        switchActivity();
    }

    private void switchActivity() {
        //TODO: This is shows how to obtain data from the ArrayList
        Log.v(LOG_TAG, "" + currenciesList.size());
        for (int i = 0; i < currenciesList.size(); i++) {
            Log.v(LOG_TAG, "" +
                    currenciesList.get(i).getBaseCurrency() + ", " +
                    currenciesList.get(i).getConvertedCurrency() + ", " +
                    currenciesList.get(i).getDateOfExchange() + ", " +
                    currenciesList.get(i).getExchangeRate()
            );
        }
    }

    /**
     * If the Loader is reset (i.e. through orientation change), handle that in this method
     * The {@param loader} to be used
     */
    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {
        Log.d(LOG_TAG, "onLoaderReset() - called");
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.v(LOG_TAG, "adapter cleared");
    }
}
