package com.zacmurphy.currencyconverter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static com.zacmurphy.currencyconverter.Currency.currenciesList;
import static com.zacmurphy.currencyconverter.R.id.amountToConvertEntryField;
import static com.zacmurphy.currencyconverter.R.id.conversionResultField;

public class MainActivity extends AppCompatActivity {

    //Tag for the log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Global instance of the Spinner, so it can be used in multiple methods in this class
    private Spinner mSpinner;

    //Global instance of the EditText, so it can be used in multiple methods in this class
    private EditText mAmountToConvertEntryField;

    //Global instance of the results field TextView, so it can be used in multiple methods in this class
    private TextView mConversionResultField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise global variables
        mSpinner = (Spinner) findViewById(R.id.currency_picker);
        mAmountToConvertEntryField = (EditText) findViewById(amountToConvertEntryField);
        mConversionResultField = (TextView) findViewById(conversionResultField);

        //Set the title of the title bar to be more appropriate
        setTitle(getResources().getText(R.string.page_1));

        //Set the typeface to the EditText
        mAmountToConvertEntryField.setTypeface(Typeface.SANS_SERIF);

        //Call this method to initiate the creation of the Spinner
        createSpinnerOptions();

        //Get the spinner as an object and call the OnItemSelectedListener on it to create the listener
        mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        Log.v(LOG_TAG, "Listener set on spinner");

        //Create a new button object constructor
        Button button = (Button) findViewById(R.id.button_convert);
        //Set a listener to it and create a new method
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Do this when the button is clicked
                Log.v(LOG_TAG, "Convert button click was registered");
                onConvertClick();
            }
        });
        Log.v(LOG_TAG, "Listener set on 'Convert' button");

        //Set a listener on the EditText
        mAmountToConvertEntryField.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == mAmountToConvertEntryField.getId()) {
                    //Make the cursor visible
                    mAmountToConvertEntryField.setCursorVisible(true);
                }
            }
        });
        Log.v(LOG_TAG, "Listener set on EditText");

        //Set a listener on the done button on the keyboard
        mAmountToConvertEntryField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Run the calculations process
                    Log.v(LOG_TAG, "Done button click was registered");
                    onConvertClick();

                    //Hide the cursor
                    mAmountToConvertEntryField.setCursorVisible(false);
                }
                return false;
            }
        });
        Log.v(LOG_TAG, "Listener set on 'Done' button");
    }

    /**
     * The method that creates the spinner options
     */
    public void createSpinnerOptions() {
        Log.d(LOG_TAG, "createSpinnerOptions - called");
        //Create an ArrayAdapter based on the CurrencyAdapter using the list of currencies
        ArrayAdapter adapter = new CurrencyAdapter(this, currenciesList);

        //Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }

    /**
     * Custom method that handles what happens when the convert button is clicked
     */
    private void onConvertClick() {
        Log.d(LOG_TAG, "onConvertClick - called");
        //Hide the cursor
        mAmountToConvertEntryField.setCursorVisible(false);

        //Get the country code of the currently selected spinner option
        String currentCountryCode = getSpinnerOption();

        //Get the relevant symbol for the currency
        String relevantCurrencySymbol = getCurrencySymbol(currentCountryCode);

        //Perform the required calculations
        double exchangedValue = getExchangedValue();

        //Change the text to display the correct values
        setResultFieldText(relevantCurrencySymbol, exchangedValue);
    }

    /**
     * Custom method
     * @return the currently selected option of the spinner
     */
    private String getSpinnerOption() {
        Log.d(LOG_TAG, "getSpinnerOption - called");
        //Get the currently selected item's position
        int position = mSpinner.getSelectedItemPosition();

        Log.v(LOG_TAG, "Selected item: " + currenciesList.get(position).getConvertedCurrency());
        //Use that position to look up the respective item in the list and return the corresponding country code
        return currenciesList.get(position).getConvertedCurrency();
    }

    /**
     * Custom method that takes in the {@param countryCode}
     * @return the relevant currency symbol associated with the countryCode
     */
    private String getCurrencySymbol(String countryCode) {
        Log.d(LOG_TAG, "getCurrencySymbol - called");
        //Declare private variables
        String symbol;
        int symbolId;

        //Append the country code to the prefix "symbol_" to get the string name, specify the resource type and package name - which give the string resource
        symbolId = getApplicationContext().getResources().getIdentifier("symbol_" + countryCode, "string", getPackageName());

        //As long as there is an existing string resource available
        if (symbolId != 0) {
            //Get the resource
            symbol = getResources().getString(symbolId);
        } else {
            //Assign the country code
            symbol = countryCode;
        }

        Log.v(LOG_TAG, "symbol: " + symbol);
        return symbol;
    }

    /**
     * Custom method, using the exchange rate and user entered value
     * @return the exchanged value for the user
     */
    private double getExchangedValue() {
        Log.d(LOG_TAG, "getExchangedValue - called");
        //Get the values from the EditText
        String userEnteredQuantity = mAmountToConvertEntryField.getText().toString();
        Log.v(LOG_TAG, "userEnteredQuantity: " + userEnteredQuantity + "|END");

        double valueToConvert;
        //Check if the user input is blank
        if (userEnteredQuantity.isEmpty()) {
            valueToConvert = 0;
        } else {
            if (userEnteredQuantity.equals(".")) {
                valueToConvert = 0;
                //Notify the user of their error
                mAmountToConvertEntryField.setError(getString(R.string.error_invalidChar));
            } else {
                valueToConvert = Double.parseDouble(userEnteredQuantity);
            }
        }
        Log.v(LOG_TAG, "valueToConvert: " + valueToConvert);

        //Get the currently selected item's position
        int position = mSpinner.getSelectedItemPosition();

        //Get the exchange rate for the particular currency
        double exchangeRate = currenciesList.get(position).getExchangeRate();
        Log.v(LOG_TAG, "exchangeRate: " + exchangeRate);

        Log.v(LOG_TAG, "exchangedValue: " + valueToConvert * exchangeRate);
        return valueToConvert * exchangeRate;
    }

    /**
     * Custom method that outputs the result from the conversion
     * @param relevantCurrencySymbol input of the relevant currency symbol to use
     * @param exchangedValue input of the exchanged value to show the user
     */
    private void setResultFieldText(String relevantCurrencySymbol, double exchangedValue) {
        Log.d(LOG_TAG, "setResultFieldText - called");

        //Set the text based on the value being input
        if (exchangedValue == 0.0) {
            mConversionResultField.setText(relevantCurrencySymbol);
        } else {
            mConversionResultField.setText(getString(R.string.content_convertedValueText, relevantCurrencySymbol, exchangedValue));
        }
    }

    /**
     * Create the options menu in the action bar
     * using {@param menu} as a resource
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu - called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle item selections of the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected - called");
        //Switch statement
        switch (item.getItemId()) {
//            //When the list icon is selected:
//            case R.id.action_conversionNav:
//                //Open the second page
//                changeToSavedConversions();
//                return true;

            //When the refresh icon is selected:
            case R.id.action_resetContent:
                Log.v(LOG_TAG, "Reset the page contents");
                //Refresh the app
                resetContent();
                return true;

            //When the help icon is selected:
            case R.id.action_help:
                Log.v(LOG_TAG, "Nav to help");
                //Open the help page
                changeToHelpPage();
                return true;

            //When the refresh option is selected:
            case R.id.action_refresh:
                Log.v(LOG_TAG, "Update conversion rates");
                //Refresh the conversion rates
                refreshConversionRates();
                return true;

            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method that navigates the user to the help page, using an intent
     */
    private void changeToHelpPage() {
        Log.d(LOG_TAG, "changeToHelpPage - called");
        //Create a new Intent object constructor, populate it with HelpActivity.class
        Intent intent = new Intent(this, HelpActivity.class);
        //Start that new intent
        startActivity(intent);
    }

//    /**
//     * Method that navigates the user to the second page, using an intent
//     */
//    private void changeToSavedConversions() {
//        //Create a new Intent object constructor, populate it with SecondActivity.class
//        Intent intent = new Intent(this, SecondActivity.class);
//        //Start that new intent
//        startActivity(intent);
//    }

    /**
     * Custom method, reset the changed fields
     */
    private void resetContent() {
        Log.d(LOG_TAG, "refreshApp - called");
        //Set the EditText field to be empty
        mAmountToConvertEntryField.setText(null);

        //Reset the Spinner value the default currency
        mSpinner.setSelection(0);

        //Reset the values in the results field
        mConversionResultField.setText(getCurrencySymbol(getSpinnerOption()));

        //Hide the cursor
        mAmountToConvertEntryField.setCursorVisible(false);
    }

    /**
     * Custom method that refreshes the currency rates
     */
    private void refreshConversionRates() {
        Log.d(LOG_TAG, "refreshConversionRates - called");
        //Clear the existing currencies, so duplicates are not produced
        currenciesList.clear();

        //Create a new Intent object constructor, populate it with MainActivity.class
        Intent intent = new Intent(this, LoadingActivity.class);
        //Make sure the user cannot navigate back to this activity by using the back button
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Start that new intent
        startActivity(intent);
        //Close the current activity
        finish();
    }

    /**
     * This sub-class controls what happens when the listener detects something has been selected
     */
    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Log.d(LOG_TAG, "Spinner item selected");
            //When an item is selected, do this:
            onConvertClick();
        }

        public void onNothingSelected(AdapterView parent) {
            Log.d(LOG_TAG, "no spinner options selected");
            // Do nothing when nothing is selected.
        }
    }
}
