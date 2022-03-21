package com.example.whatshappeningat;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class API {

    private static final String TAG = "API";

    private static final String OPENWEATHERMAP_API_KEY = "d8965512f55e3a250abe4a05eede8f58";
    private static final String NEWS_API = "pub_5541b0b5040a57d9f1c3f7da5a8bafebd7c3";

    /*
    -------------------- General API Functions --------------------
     */

    private static String getAPIResponse(URL url) throws APIException {

        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                throw new APIException("Nothing to retrieve from InputStream");
            }

        } catch (IOException e) {
            throw new APIException(e.getMessage(), e.getCause());

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /*
    -------------------- Location API Functions --------------------
     */

    //TODO - handle bad inputs

    private static URL buildGeocodingURL(String cityName) throws MalformedURLException {
        String string = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName +
                "&limit=1&appid=" + OPENWEATHERMAP_API_KEY;
        Log.d(TAG, "URL: " + string);
        return new URL(string);
    }

    private static double[] parseGeoResponse(String response) throws JSONException {
        Log.d(TAG, "parseGeoResponse running...");
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return new double[] {jsonObject.getDouble("lat"), jsonObject.getDouble("lon")};
    }

    private static String parseGeoResponseCountryCode(String response) throws JSONException {
        Log.d(TAG, "parseGeoResponseCountryCode running...");
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getString("country");
    }

    public static double[] getCoords(String cityName) throws APIException {
        try {
            Log.d(TAG, "getCoords() building url...");
            URL url = buildGeocodingURL(cityName);
            Log.d(TAG, "getCoords() getting api response...");
            String response = getAPIResponse(url);
            Log.d(TAG, "getCoords() parsing api response...");
            double[] coords = parseGeoResponse(response);
            Log.d(TAG, "getCoords() -> {" + coords[0] + ", " + coords[1] + "}");
            return coords;
        } catch (Exception e) {
            Log.e(TAG, "geoCoding api error:\n" + e.toString());
            throw new APIException(e.getMessage(), e.getCause());
        }
    }

    public static String getCountryCode(String cityName) throws APIException {
        try {
            Log.d(TAG, "getCountryCode() building url...");
            URL url = buildGeocodingURL(cityName);
            Log.d(TAG, "getCountryCode() getting api response...");
            String response = getAPIResponse(url);
            Log.d(TAG, "getCountryCode() parsing api response...");
            String result = parseGeoResponseCountryCode(response);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "geoCoding api error:\n" + e.toString());
            throw new APIException(e.getMessage(), e.getCause());
        }
    }


    /*
    -------------------- Weather API Functions --------------------
     */

    private static URL buildWeatherURL(double[] coords) throws MalformedURLException {
        String string = "https://api.openweathermap.org/data/2.5/onecall?lat=" + coords[0] +"&lon=" + coords[1] +
                "&exclude=current,minutely,hourly,alerts&appid=" + OPENWEATHERMAP_API_KEY;
        return new URL(string);
    }

    private static ArrayList<String[]> parseWeatherResponse(String response) throws JSONException {
        ArrayList<String[]> arrayList = new ArrayList<>();

        JSONObject outerJSONObject = new JSONObject(response);
        JSONArray jsonArray = outerJSONObject.getJSONArray("daily");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject day = jsonArray.getJSONObject(i);

            JSONObject temp = day.getJSONObject("temp");
            JSONObject desc = day.getJSONArray("weather").getJSONObject(0);

            String tempValue;
            //TODO -
            if (false) {
                // Fahrenheit
                tempValue = String.valueOf( kelvinToF(temp.getDouble("day")) );
            } else {
                // Celsius
                tempValue = String.valueOf( kelvinToC(temp.getDouble("day")) ) + '\u00B0' + 'C';
            }

            String[] info = {
                    unixTimeToDate( day.getInt("dt") ),
                    tempValue,
                    desc.getString("description")
            };

            arrayList.add(info);
        }

        return arrayList;
    }

    public static ArrayList<String[]> getWeather(double[] coords) throws APIException {
        try {
            Log.d(TAG, "getWeather() building url...");
            URL url = buildWeatherURL(coords);

            Log.d(TAG, "getWeather() getting response...");
            String response = getAPIResponse(url);

            Log.d(TAG, "getWeather() parsing response...");
            ArrayList<String[]> weather = parseWeatherResponse(response);

            Log.d(TAG, "getWEather() returning results...");
            return weather;

        } catch (Exception e) {
            Log.e(TAG, "weather api error:\n" + e.toString());
            throw new APIException(e.getMessage(), e.getCause());
        }
    }

    private static int kelvinToC(double k) {
        return (int) k - 273;
    }

    private static int kelvinToF(double k) {
        return (int) (kelvinToC(k)*(9/5)+32);
    }

    private static String unixTimeToDate(long unixTime) {
        java.util.Date time = new java.util.Date( unixTime*1000 );
        return time.toString();
    }



    /*
    -------------------- News API Functions --------------------
     */

    private static URL buildNewsURl(String countryCode) throws MalformedURLException {
        String string = "https://newsdata.io/api/1/news?apikey=" + NEWS_API +
                "&country=" + countryCode;
        return new URL(string);
    }

    private  static ArrayList<String[]> parseNewsResponse(String response) throws JSONException {
        ArrayList<String[]> arrayList = new ArrayList<>();

        int articleLimit;
        JSONObject article;

        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        if (jsonObject.getInt("totalResults") > 50) {
            articleLimit = 50;
        } else {
            articleLimit = jsonArray.length();
        }

        for (int i = 0; i < 10; i++){
            article = jsonArray.getJSONObject(i);

            arrayList.add( new String[]{article.getString("title"), article.getString("description"), article.getString("link")} );
        }

        return arrayList;
    }

    public static ArrayList<String[]> getNews(String country) throws APIException {
        try {
            Log.d(TAG, "getNews() building url...");
            URL url = buildNewsURl(getCountryCode(country));

            Log.d(TAG, "getNews() getting api response...");
            String response = getAPIResponse(url);

            Log.d(TAG, "getNews() parsing api response...");
            ArrayList<String[]> result = parseNewsResponse(response);

            Log.d(TAG, "getNews() finished");
            return result;

        } catch (Exception e) {
            Log.e(TAG, "news api error:\n" + e.toString());
            throw new APIException(e.getMessage(), e.getCause());
        }
    }
}
