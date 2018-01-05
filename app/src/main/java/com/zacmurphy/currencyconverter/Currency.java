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
    public static List<Currency> currenciesList = new ArrayList<>();
    //Declare private variables
    private String mDate;
    private String mBase;
    private String mCurrencyKey;
    private double mCurrencyValue;
    private int mPriority;

    /**
     * Create the constructor for this class, a constructor creates an instance of a class
     * This constructor will create an instance of two Strings and a double
     *
     * @param date          - the date the conversion rates are from
     * @param base          - the base currency
     * @param currencyKey   - the currency being converted to
     * @param currencyValue - the exchange rate
     */
    public Currency(String date, String base, String currencyKey, double currencyValue, int priority) {
        Log.d(LOG_TAG, "Constructor - called");
        mDate = date;
        mBase = base;
        mCurrencyKey = currencyKey;
        mCurrencyValue = currencyValue;
        mPriority = priority;
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

    /**
     * @return the priority of the currency, determined in QueryUtils
     */
    public int getPriority() {
        return mPriority;
    }
}