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

public class MainActivity extends AppCompatActivity {

    //Tag for the log messages
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the title of the title bar to be more appropriate
        setTitle(getResources().getText(R.string.page_1));

        //Call this method to initiate the creation of the Spinner
        createSpinnerOptions();

        //Get the spinner as an object and call the OnItemSelectedListener on it to create the listener
        Spinner spinner = (Spinner) findViewById(R.id.currency_picker);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        //Create a new button object constructor
        Button button = (Button) findViewById(R.id.button_convert);
        //Set a listener to it and create a new method
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Do this when the button is clicked
//                Log.d("TEST MESSAGE:", "Convert button click was registered");
                onConvertClick();
            }
        });
    }

    /**
     * Custom method that handles what happens when the convert button is clicked
     */
    private void onConvertClick() {
        Log.d("TEST MESSAGE:", "onConvertClick was called");
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
//        Log.i("TEST MESSAGE:", "getSpinnerOption was called");
        //Create a spinner object constructor
        Spinner spinner = (Spinner) findViewById(R.id.currency_picker);
        //Get the current item that is selected in the spinner
//        Log.i("TEST MESSAGE:", "Value to return: " + spinner.getSelectedItem().toString());
        return spinner.getSelectedItem().toString();
    }

    /**
     * Custom method
     *
     * @param country input of the country
     * @return the relevant currency symbol associated with the country
     */
    private String getCurrencySymbol(String country) {
        //Use switch statement to determine the correct symbol for the
        // currency and return it
        switch (country) {
            //Euros
            case "European Euro":
                return getResources().getString(R.string.symbol_EUR);

            //US Dollars
            case "US Dollar":
                return getResources().getString(R.string.symbol_USD);

            //Japanese Yen
            case "Japanese Yen":
                return getResources().getString(R.string.symbol_JPY);

            default:
                return getResources().getString(R.string.symbol_GBP);
        }
    }

    /**
     * Custom method that outputs the result from the conversion
     *
     * @param relevantCurrencySymbol input of the relevant currency symbol to use
     */
    private void setResultFieldText(String relevantCurrencySymbol) {
        //Create an object constructor for the text-field that is changed
        TextView conversionResultField = (TextView) findViewById(R.id.conversionResultField);

        //Create an object constructor for the EditText
        EditText amountToConvertEntryField = (EditText) findViewById(R.id.amountToConvertEntryField);
        //Get the values from the EditText
        String userEnteredQuantity = amountToConvertEntryField.getText().toString();

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handle item selections of the action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Switch statement
        switch (item.getItemId()) {
//            //When the list icon is selected:
//            case R.id.action_conversionNav:
//                //Open the second page
//                changeToSavedConversions();
//                return true;

            //When the refresh icon is selected:
            case R.id.action_refresh:
                //Refresh the app
                refreshApp();
                return true;

            //When the help icon is selected:
            case R.id.action_help:
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
        //Create a new Intent object constructor, populate it with ThirdActivity.class
        Intent intent = new Intent(this, HelpActivity.class);
        //Start that new intent
        startActivity(intent);
    }

    /**
     * Custom method, refresh the conversion rates and reset the changed fields
     */
    private void refreshApp() {
        //Create an object constructor for the EditText
        EditText amountToConvertEntryField = (EditText) findViewById(R.id.amountToConvertEntryField);
        //Set the field to be empty
        amountToConvertEntryField.setText(null);

        //Create a spinner object constructor
        Spinner spinner = (Spinner) findViewById(R.id.currency_picker);
        //Reset the value to Euro (default)
        spinner.setSelection(0);

        //Reset the values in the results field
        setResultFieldText(getResources().getString(R.string.symbol_EUR));

        //TODO: Implement refreshing of conversion rates
    }

    /**
     * The method that creates the spinner options
     */
    public void createSpinnerOptions() {
        //Create the spinner as an object
        Spinner spinner = (Spinner) findViewById(R.id.currency_picker);

        // Create an ArrayAdapter using the the array of options and the style of the text before selection layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_currencies, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /**
     * This sub-class controls what happens when the listener detects something has been selected
     */
    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            //When an item is selected, do this:
            onConvertClick();
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing when nothing is selected.
        }
    }
}
