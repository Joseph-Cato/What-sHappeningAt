package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Location extends View {

    private final String LOCATION_NAME;
    private int LATITUDE;
    private int LONGITUDE;
    private String areaCode;

    public Location(Context context, AttributeSet attrs, String LOCATION_NAME) {
        super(context, attrs);
        this.LOCATION_NAME = LOCATION_NAME;

        OkHttpClient client = new OkHttpClient();
        String url ="http://api.openweathermap.org/geo/1.0/direct?q=" + LOCATION_NAME + "&limit=1&appid=" + MainActivity.getOpenWeatherApiKey();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //TODO - error handling
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        LATITUDE = jsonObject.getInt("lat");
                        LONGITUDE = jsonObject.getInt("lon");
                        areaCode = jsonObject.getString("country");
                    } catch (JSONException e) {
                        //TODO - error handling
                        e.printStackTrace();
                    }

                }

            }
        });

    }
}
