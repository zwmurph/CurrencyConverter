package com.zacmurphy.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static com.zacmurphy.currencyconverter.R.id.amountToConvertEntryField;

public class MainActivity extends AppCompatActivity {

    //Tag for the log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Global instance of the Spinner, so it can be used in multiple methods in this class
    private Spinner mSpinner;

    //Global instance of the EditText, so it can be used in multiple methods in this class
    private EditText mAmountToConvertEntryField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise global variables
        mSpinner = (Spinner) findViewById(R.id.currency_picker);
        mAmountToConvertEntryField = (EditText) findViewById(amountToConvertEntryField);

        //Set the title of the title bar to be more appropriate
        setTitle(getResources().getText(R.string.page_1));

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
                Log.v("TEST MESSAGE:", "Convert button click was registered");
                onConvertClick();
            }
        });
        Log.v(LOG_TAG, "Listner set on convert button");
    }

    /**
     * The method that creates the spinner options
     */
    public void createSpinnerOptions() {
        Log.d(LOG_TAG, "createSpinnerOptions - called");
        //Create an ArrayAdapter using the array of options and the style of the text before selection layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_currencies, R.layout.spinner_item);

        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
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

    /**
     * Custom method that handles what happens when the convert button is clicked
     */
    private void onConvertClick() {
        Log.d(LOG_TAG, "onConvertClick - called");
        //Get the value of the current spinner option
        String currentSpinnerOption = getSpinnerOption();
        //Get the relevant symbol for the currency
        String relevantCurrencySymbol = getCurrencySymbol(currentSpinnerOption);

        //Change the text to display the correct values
        setResultFieldText(relevantCurrencySymbol);
    }

    /**
     * Custom method
     *
     * @return the currently selected option of the spinner
     */
    private String getSpinnerOption() {
        Log.d(LOG_TAG, "getSpinnerOption - called");
        //Get the current item that is selected in the spinner
        Log.v(LOG_TAG, "Value to return: " + mSpinner.getSelectedItem().toString());
        return mSpinner.getSelectedItem().toString();
    }

    /**
     * Custom method
     *
     * @param country input of the country
     * @return the relevant currency symbol associated with the country
     */
    private String getCurrencySymbol(String country) {
        Log.d(LOG_TAG, "getCurrencySymbol - called");
        //Use switch statement to determine the correct symbol for the
        // currency and return it
        switch (country) {
            //Euros
            case "European Euro":
                Log.v(LOG_TAG, "Value to return: " + getResources().getString(R.string.symbol_EUR));
                return getResources().getString(R.string.symbol_EUR);

            //US Dollars
            case "US Dollar":
                Log.v(LOG_TAG, "Value to return: " + getResources().getString(R.string.symbol_USD));
                return getResources().getString(R.string.symbol_USD);

            //Japanese Yen
            case "Japanese Yen":
                Log.v(LOG_TAG, "Value to return: " + getResources().getString(R.string.symbol_JPY));
                return getResources().getString(R.string.symbol_JPY);

            default:
                Log.v(LOG_TAG, "Value to return: " + getResources().getString(R.string.symbol_GBP));
                return getResources().getString(R.string.symbol_GBP);
        }
    }

    /**
     * Custom method that outputs the result from the conversion
     *
     * @param relevantCurrencySymbol input of the relevant currency symbol to use
     */
    private void setResultFieldText(String relevantCurrencySymbol) {
        Log.d(LOG_TAG, "setResultFieldText - called");
        //Create an object constructor for the text-field that is changed
        TextView conversionResultField = (TextView) findViewById(R.id.conversionResultField);

        //Get the values from the EditText
        String userEnteredQuantity = mAmountToConvertEntryField.getText().toString();
        Log.v(LOG_TAG, "userEnteredQuantity: " + userEnteredQuantity);

        //TODO: implement proper calculations into where the text-field is changed
        //Set the text-field text
        conversionResultField.setText(relevantCurrencySymbol + userEnteredQuantity);
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
            case R.id.action_refresh:
                Log.v(LOG_TAG, "Refresh the app");
                //Refresh the app
                refreshApp();
                return true;

            //When the help icon is selected:
            case R.id.action_help:
                Log.v(LOG_TAG, "Nav to help");
                //Open the help page
                changeToHelpPage();
                return true;

            default:
                // The user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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
     * Method that navigates the user to the help page, using an intent
     */
    private void changeToHelpPage() {
        Log.d(LOG_TAG, "changeToHelpPage - called");
        //Create a new Intent object constructor, populate it with HelpActivity.class
        Intent intent = new Intent(this, HelpActivity.class);
        //Start that new intent
        startActivity(intent);
    }

    /**
     * Custom method, refresh the conversion rates and reset the changed fields
     */
    private void refreshApp() {
        Log.d(LOG_TAG, "refreshApp - called");
        //Set the EditText field to be empty
        mAmountToConvertEntryField.setText(null);

        //Reset the Spinner value to Euro (default)
        mSpinner.setSelection(0);

        //Reset the values in the results field
        setResultFieldText(getResources().getString(R.string.symbol_EUR));

        //TODO: Implement refreshing of conversion rates
    }
}
