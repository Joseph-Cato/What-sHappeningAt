package com.example.whatshappeningat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    private RecyclerView weather;

    private String cityName;
    private static volatile double[] coords;
    private static volatile ArrayList<String[]> weatherInfo;


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
                weatherInfo = API.getWeather(coords);
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
            Log.d(TAG, "(weather) onPostExecute running...");
            setWeatherAdapter();
            super.onPostExecute(strings);
        }
    }

    //TODO - news async

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
        while (coords == null) {
            //Do Nothing
        }
        Log.d(TAG, "coords != null");

        // Weather
        weather = findViewById(R.id.weather);
        WeatherAsyncTask weatherTask = new WeatherAsyncTask();
        weatherTask.execute(coords);

        // News

    }

    private void setWeatherAdapter() {
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(getApplicationContext(),weatherInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        weather.setLayoutManager(layoutManager);
        weather.setItemAnimator(new DefaultItemAnimator());
        weather.setAdapter(adapter);
    }

    private void initWeatherRecyclerView() {
        Log.d(TAG, "initWeatherRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.weather);
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(this, weatherInfo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}