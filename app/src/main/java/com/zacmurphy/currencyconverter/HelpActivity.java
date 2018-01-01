package com.zacmurphy.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import static com.zacmurphy.currencyconverter.Currency.currenciesList;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView exchangeRateDateView = (TextView) findViewById(R.id.exchangeRateDate);
        exchangeRateDateView.setText(currenciesList.get(0).getDateOfExchange());
    }
}
