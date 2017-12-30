package com.zacmurphy.currencyconverter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    CurrencyAdapter(Activity context, List<Currency> currencies) {
        // This initialises the ArrayAdapter's internal storage for the context and the list.
        // The second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for four TextViews the adapter is not
        // going to use this second argument, so it can be any value.
        super(context, 0, currencies);
        Log.d(LOG_TAG, "Constructor - called");
    }

    /**
     * Overridden method to produce the contents of the spinner when clicked on
     * @param position - the specific item in the list
     * @param convertView - the view to change
     * @param parent - the parent activity
     * @return the completed view
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(LOG_TAG, "getDropDownView - called");
        //Get the data item for this position
        Currency currentExchange = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        //Lookup view for data population
        TextView country = convertView.findViewById(R.id.spinnerDropDownText);
        ImageView icon = convertView.findViewById(R.id.spinnerDropDownImage);

        //Set the correct text to the TextView
        country.setText(currentExchange.getConvertedCurrency());

        //Get the resource ID
        int resId = getObjectResourceId(currentExchange.getConvertedCurrency());

        //If there is no resource for the item, 0 will be returned
        if (resId != 0) {
            //Set the resource to the ImageView for this item
            icon.setImageResource(resId);
        } else {
            //Get rid of the view
            icon.setVisibility(View.GONE);
        }

        //Return the completed view to render on-screen
        return convertView;
    }

    /**
     * Overridden method to produce the contents of the spinner before it's clicked on
     * @param position - the specific item in the list
     * @param convertView - the view to change
     * @param parent - the parent activity
     * @return the completed view
     */
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

        //Lookup view for data population
        TextView country = convertView.findViewById(R.id.spinnerText);
        ImageView icon = convertView.findViewById(R.id.spinnerImage);

        //Set the correct text to the TextView
        country.setText(currentExchange.getConvertedCurrency());

        //Get the resource ID
        int resId = getObjectResourceId(currentExchange.getConvertedCurrency());

        //If there is no resource for the item, 0 will be returned
        if (resId != 0) {
            //Set the resource to the ImageView for this item
            icon.setImageResource(resId);
        } else {
            //Get rid of the view
            icon.setVisibility(View.GONE);
        }

        //Return the completed view to render on-screen
        return convertView;
    }

    /**
     * @return the resource ID based on the {@param countryCode}
     */
    private int getObjectResourceId(String countryCode) {
        Log.d(LOG_TAG, "getObjectResourceId - called");
        Log.v(LOG_TAG, "resourceId: " + getContext().getResources().getIdentifier("flag_" + countryCode.toLowerCase(), "drawable", getContext().getPackageName()));
        //Append the country code to the prefix "flag_" to get the file name, specify the resource type and package name - which return the drawable resource
        return getContext().getResources().getIdentifier("flag_" + countryCode.toLowerCase(), "drawable", getContext().getPackageName());
    }

//    //TODO: Implement this method?:
//    /**
//     * @return the country name based on the {@param countryCode}
//     */
//    private String getCountryName(String countryCode) {
//
//        return x;
//    }
}