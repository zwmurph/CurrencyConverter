package com.zacmurphy.currencyconverter;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving currency data from Fixer.IO
 */
class QueryUtils {

    //Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //Public Boolean for error detection during the data loading process
    static boolean ERROR_OCCURRED;

    /**
     * Query Fixer.IO and return an {@link List<Currency>} object to represent a single exchange rate.
     */
    public static List<Currency> fetchCurrencyData(String requestUrl) {
        Log.d(LOG_TAG, "fetchCurrencyData - called");
        //Set the variable to false, until an error occurs and it is set to true
        ERROR_OCCURRED = false;

        //Create a URL object
        URL url = createUrl(requestUrl);
        Log.v(LOG_TAG, "url: " + url);

        //Perform a HTTP request to the URL and receive a JSON response back
        //Initialise variable
        String jsonResponse = null;

        //Try the creation of a JSON response from the URL, if not catch the exception
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            ERROR_OCCURRED = true;
            //Log the error
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        //Extract and return the relevant fields from the JSON response and create a List<Currency> object
        return extractFeaturesFromJson(jsonResponse);
    }

    /**
     * @return a new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        Log.d(LOG_TAG, "createUrl - called");
        //Initialise variable
        URL url = null;

        //Try the creation of a URL, if not catch the exception
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            ERROR_OCCURRED = true;
            //Log the error
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        Log.v(LOG_TAG, "url: " + url);
        //Return the successful URL
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *
     * @throws IOException if something goes wrong
     */
    private static String makeHttpRequest(URL url) throws IOException {
        Log.d(LOG_TAG, "makeHttpRequest - called");
        //Initialise variable
        String jsonResponse = "";

        //If the URL is null, then return early with an empty string
        if (url == null) {
            ERROR_OCCURRED = true;
            Log.v(LOG_TAG, "URL is null");
            return jsonResponse;
        }

        //Initialise variables
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //Try connecting to the URL and reading the response, else catch the exception
        try {
            //Open the connection to the URL
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(LOG_TAG, "urlConnection opened");

            //Set timeouts so if the connections expire before data is available, an error is thrown
            //Makes ure the user is never waiting for long periods of time if there is no data
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            Log.d(LOG_TAG, "timeouts set");

            //Set the request method, as data wants to be retrieved
            urlConnection.setRequestMethod("GET");
            Log.d(LOG_TAG, "request method set");

            //Make the connection
            urlConnection.connect();
            Log.d(LOG_TAG, "url connection connecting");

            //If the request was successful (response code 200), then read the input and parse the response
            if (urlConnection.getResponseCode() == 200) {
                Log.v(LOG_TAG, "Response code was 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.v(LOG_TAG, "jsonResponse: " + jsonResponse);
            } else {
                ERROR_OCCURRED = true;
                //Log the error
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            ERROR_OCCURRED = true;
            //Log the error
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            //As long as there is an open connection
            if (urlConnection != null) {
                //Close the URL connection
                urlConnection.disconnect();
                Log.v(LOG_TAG, "urlConnection disconnected");
            }
            //As long as the inputStream is open
            if (inputStream != null) {
                //Close the inputStream
                inputStream.close();
                Log.v(LOG_TAG, "inputStream closed");
            }
        }
        //Return the JSON response
        Log.v(LOG_TAG, "jsonResponse: " + jsonResponse);
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.d(LOG_TAG, "readFromStream - called");
        //Initialise variables / create a new String Builder (S.B.)
        //S.B. builds one String bit by bit instead of appending more information each time through other variables
        StringBuilder output = new StringBuilder();

        //As long as the inputStream is not empty
        if (inputStream != null) {
            Log.v(LOG_TAG, "inputStream is not null");
            //Create a new InputStreamReader, using the inputStream and "UTF-8" character set
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            //Create a buffered reader, which stores information about the characters around each character
            BufferedReader reader = new BufferedReader(inputStreamReader);

            //Get the line of text
            String line = reader.readLine();

            //While the line isn't empty
            while (line != null) {
                //Append the line to the output
                output.append(line);
                //Goto the next line
                line = reader.readLine();
            }
        }
        //Return the completed output in String format
        return output.toString();
    }

    /**
     * @return an {@link List<Currency>} item by parsing out information
     * about the exchange rates from the input jsonResponse string.
     */
    private static List<Currency> extractFeaturesFromJson(String jsonResponse) {
        Log.d(LOG_TAG, "extractFeaturesFromJson - called");
        //If the JSON String is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            ERROR_OCCURRED = true;
            Log.v(LOG_TAG, "JSON string is empty");
            return null;
        }

        //Create an empty ArrayList that we can start adding exchange rates to
        List<Currency> currencies = new ArrayList<>();
        //List<Currency> currencies = Currency.getArrayList();

        //Try to parse the jsonResponse, else catch the JSONException
        try {
            //Get the root of the JSON string
            JSONObject root = new JSONObject(jsonResponse);
            Log.v(LOG_TAG, "root: " + root.toString());

            //Get the base currency
            String baseCurrency = root.getString("base");
            Log.v(LOG_TAG, "baseCurrency: " + baseCurrency);

            //Get the date of the exchange rate
            String exchangeDate = root.getString("date");
            Log.v(LOG_TAG, "exchangeDate: " + exchangeDate);

            //Get the "rates" array as an object
            JSONObject rates = root.getJSONObject("rates");

            //For loop that cycles through all of the rates objects
            for (int i = 0; i < rates.length(); i++) {
                //Get the object key (country code)
                String objectKey = rates.names().getString(i);

                //Get the exchange rate value associated with that object
                double keyValue = rates.getDouble(objectKey);

                //Get the priority for each object
                int priority = getPriority(objectKey);

                //Add those details along with the above date and base to the array
                Log.v(LOG_TAG, exchangeDate + ", " + baseCurrency + ", " + objectKey + ", " + keyValue + ", " + priority);
                currencies.add(new Currency(exchangeDate, baseCurrency, objectKey, keyValue, priority));
            }
        } catch (JSONException e) {
            ERROR_OCCURRED = true;
            //Log the error
            Log.e(LOG_TAG, "Problems parsing the currency JSON results", e);
        }
        return currencies;
    }

    /**
     * Custom method that assigns a priority to a currency based on how used it is worldwide,
     * this I determined using data from the Internet
     *
     * @param currencyCode, the country to be checked
     * @return the priority level
     */
    private static int getPriority(String currencyCode) {
        Log.d(LOG_TAG, "getPriority - called");
        switch (currencyCode) {
            case "USD":
                return 0;
            case "EUR":
                return 1;
            case "JPY":
                return 2;
            case "AUD":
                return 3;
            case "CAD":
                return 4;
            case "CHF":
                return 5;
            case "CNY":
                return 6;
            default:
                return 10;
        }
    }
}