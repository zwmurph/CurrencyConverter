package com.zacmurphy.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.zacmurphy.currencyconverter.Currency.currenciesList;

public class HelpActivity extends AppCompatActivity {

    //Tag for the log messages
    private static final String LOG_TAG = HelpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Create a constructor for the TextView
        TextView exchangeRateDateView = (TextView) findViewById(R.id.exchangeRateDate);

        //String to Date conversion in Java with dd-MM-yyyy format e.g. "14-09-2011"
        String dateOfExchange = currenciesList.get(0).getDateOfExchange();

        //Set the date on the TextView
        exchangeRateDateView.setText(dateOfExchange);
    }
}
