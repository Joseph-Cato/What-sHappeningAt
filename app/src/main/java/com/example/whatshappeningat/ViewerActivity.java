package com.example.whatshappeningat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewerActivity extends AppCompatActivity {

    private static final String TAG = "ViewerActivity";

    private TextView cityNameTextView;
    //private RecyclerView weather;
    //private RecyclerView news;

    private String cityName;
    private static volatile double[] coords;
    private static volatile Boolean gotWeatherInfo = false;
    private static volatile Boolean gotNewsInfo = false;
    private static volatile ArrayList<String[]> weatherInfo;
    private static volatile ArrayList<String[]> newsInfo;


    private class GeoLocationAsyncTask extends AsyncTask<String, Integer, double[]> {

        @Override
        protected double[] doInBackground(String... strings) {
            try {
                Log.d(TAG, "(geo) doInBackground( " + strings[0] + " )...");
                coords = API.getCoords(strings[0]);
                return coords;
            } catch (APIException e) {
                Log.e(TAG, "GeoLocation APIException:\n" + e.toString());
                //Toast.makeText(getApplicationContext(), "Error Getting Data", Toast.LENGTH_LONG).show();
                return new double[0];
            }
        }

        @Override
        protected void onPostExecute(double[] doubles) {
            super.onPostExecute(doubles);
            Log.d(TAG, "(geo) onPostExecute running...");


        }
    }

    private class WeatherAsyncTask extends AsyncTask<double[], Integer, ArrayList<String[]>> {

        @Override
        protected ArrayList<String[]> doInBackground(double[]... doubles) {
            try {
                Log.d(TAG, "(weather) doInBackground running...");
                ArrayList<String[]> weatherInfo = API.getWeather(coords);
                Log.d(TAG, "(weather) doInBackground finished");
                return weatherInfo;
            } catch (APIException e) {
                Log.e(TAG, "API Exception:\n" + e.toString());
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(), "Error Retrieving Weather Data", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> strings) {
            super.onPostExecute(strings);
            Log.d(TAG, "(weather) onPostExecute running...");
            weatherInfo = strings;
            gotWeatherInfo = true;

        }
    }

    //TODO - news async
    private class NewsAsyncTask extends  AsyncTask<String, Integer, ArrayList<String[]>> {

        @Override
        protected ArrayList<String[]> doInBackground(String... strings) {
            try {
                Log.d(TAG, "(news) doInBackground running...");
                ArrayList<String[]> newsInfo = API.getNews(strings[0]);
                Log.d(TAG, "(weather) doInBackground finished");
                return newsInfo;
            } catch (APIException e) {
                Log.e(TAG, "API Exception:\n" + e.toString());
                e.printStackTrace();

                //Toast.makeText(getApplicationContext(), "Error Retrieving Weather Data", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> strings) {
            super.onPostExecute(strings);
            Log.d(TAG, "(news) onPostExecute running...");
            initNewsRecyclerView(strings);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        Intent intent = getIntent();


        //Location

        cityName = intent.getStringExtra("cityName");

        cityNameTextView = findViewById(R.id.cityName);
        cityNameTextView.setText( cityName );

        GeoLocationAsyncTask geoTask = new GeoLocationAsyncTask();
        geoTask.execute(cityName);

        // Weather
        WeatherAsyncTask weatherTask = new WeatherAsyncTask();
        weatherTask.execute(coords);

        while (!gotWeatherInfo) {
            // do nothing
        }

        initWeatherRecyclerView(weatherInfo);

        // News
        NewsAsyncTask newTask = new NewsAsyncTask();
        try {
            newTask.execute(API.getCountryCode(cityName));
        } catch (APIException e) {
            e.printStackTrace();
        }


    }

    /*
    private void setWeatherAdapter() {
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(getApplicationContext(),weatherInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        weather.setLayoutManager(layoutManager);
        weather.setItemAnimator(new DefaultItemAnimator());
        weather.setAdapter(adapter);
    }
    */

    private void initWeatherRecyclerView(ArrayList<String[]> weatherInfo) {
        Log.d(TAG, "initWeatherRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.weather);
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(this, weatherInfo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initNewsRecyclerView(ArrayList<String[]> newsInfo) {
        Log.d(TAG, "initNewsRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.news);
        NewsRecyclerViewAdapter newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, newsInfo);
        recyclerView.setAdapter(newsRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void openURL(String url, View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        v.getContext().startActivity(browserIntent);
    }



}