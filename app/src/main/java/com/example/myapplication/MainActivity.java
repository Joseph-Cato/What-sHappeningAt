package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private String[] locations;
    private static String openWeatherApiKey = "eec9b50914cd3087c322220c797710b0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locations = PrefConfig.loadLocationPref(getApplicationContext());

        final Button location1 = (Button) findViewById(R.id.country1);
        configCountryButton(location1, 0);

        final Button location2 = (Button) findViewById(R.id.country2);
        configCountryButton(location2, 1);

        final Button location3 = (Button) findViewById(R.id.country3);
        configCountryButton(location3, 2);

    }

    private void configCountryButton(Button button, int countryNum) {
        if (locations[countryNum] != null) {
            button.append(locations[countryNum]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO - inflate country info view
                }
            });
        } else {
            button.append("Add Country");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO - add country functionality
                }
            });
        }
    }

    public static String getOpenWeatherApiKey(){
        return openWeatherApiKey;
    }

    public void updateMainView(String country) {
        // TODO -
    }
}

