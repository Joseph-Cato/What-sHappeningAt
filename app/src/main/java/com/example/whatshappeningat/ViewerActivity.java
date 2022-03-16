package com.example.whatshappeningat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewerActivity extends AppCompatActivity {

    private static final String TAG = "ViewerActivity";

    private TextView cityNameTextView;
    private RecyclerView weather;

    private String cityName;
    private double[] coords;
    private ArrayList<String[]> weatherInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        Intent intent = getIntent();

        /*
        ----------------------------- Location -----------------------------
         */

        cityNameTextView = findViewById(R.id.cityName);
        cityNameTextView.setText( intent.getStringExtra("cityName") );

        cityName = cityNameTextView.toString();

        try {
            coords = API.getCoords(cityName);
        } catch (APIException e) {
            Log.e(TAG, "GeoLocation APIException:\n" + e.toString());
            Toast.makeText(getApplicationContext(), "Error Getting Data", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        ----------------------------- Weather -----------------------------
         */
        weather = findViewById(R.id.weather);

        try {
            weatherInfo = API.getWeather(coords);

            initWeatherRecyclerView();

        } catch (APIException e) {
            Log.e(TAG, "API Exception:\n" + e.toString());
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Error Retriving Weather Data", Toast.LENGTH_LONG).show();
        }


    }

    private void initWeatherRecyclerView() {
        Log.d(TAG, "initWeatherRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.weather);
        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(this, weatherInfo);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}