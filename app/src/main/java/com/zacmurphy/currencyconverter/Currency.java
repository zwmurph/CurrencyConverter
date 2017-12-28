package com.zacmurphy.currencyconverter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The Currency class that creates and provides methods that return parts of the ArrayList when called
 * by other parts of the app
 */
class Currency {

    //Tag for the log messages
    private static final String LOG_TAG = Currency.class.getSimpleName();

    //Declare private variables
    private String mDate;
    private String mBase;
    private String mCurrencyKey;
    private double mCurrencyValue;
    public static List<Currency> currenciesList = new ArrayList<>();

    /**
     * Create the constructor for this class, a constructor creates an instance of a class
     * This constructor will create an instance of two Strings and a double
     *
     * @param date
     * @param base
     * @param currencyKey
     * @param currencyValue
     */
    public Currency(String date, String base, String currencyKey, double currencyValue) {
        Log.d(LOG_TAG, "Constructor - called");
        mDate = date;
        mBase = base;
        mCurrencyKey = currencyKey;
        mCurrencyValue = currencyValue;
    }

    /**
     * @return the date that is associated with the results
     */
    public String getDateOfExchange() {
        return mDate;
    }

    /**
     * @return the base currency
     */
    public String getBaseCurrency() {
        return mBase;
    }

    /**
     * @return the currency that the conversion rate applies to
     */
    public String getConvertedCurrency() {
        return mCurrencyKey;
    }

    /**
     * @return the exchange rate that the country is providing
     */
    public double getExchangeRate() {
        return mCurrencyValue;
    }
}