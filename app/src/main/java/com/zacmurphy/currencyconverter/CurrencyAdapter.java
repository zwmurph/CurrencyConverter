package com.zacmurphy.currencyconverter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class CurrencyAdapter extends ArrayAdapter<Currency> {

    //Tag for the log messages
    private static final String LOG_TAG = CurrencyAdapter.class.getSimpleName();

    /**
     * A custom constructor.
     *
     * @param context    is used to inflate the layout file
     * @param currencies is the data we want to populate into the lists
     */
    public CurrencyAdapter(Activity context, List<Currency> currencies) {
        // This initialises the ArrayAdapter's internal storage for the context and the list.
        // The second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for four TextViews the adapter is not
        // going to use this second argument, so it can be any value.
        super(context, 0, currencies);
        Log.d(LOG_TAG, "Constructor - called");
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(LOG_TAG, "getDropDownView - called");
        //Get the data item for this position
        Currency currentExchange = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        //Get the TextView to populate from the layout
        TextView item = convertView.findViewById(R.id.spinnerDropDownItem);

        //Set the correct text to the TextView
        item.setText(currentExchange.getConvertedCurrency());

        //Return the completed view to render on-screen
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(LOG_TAG, "getView() - called");
        //Get the data item for this position
        Currency currentExchange = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        //Get the TextView to populate from the layout
        TextView item = convertView.findViewById(R.id.spinnerItem);

        //Set the correct text to the TextView
        item.setText(currentExchange.getConvertedCurrency());

        //Return the completed view to render on-screen
        return convertView;
    }
}