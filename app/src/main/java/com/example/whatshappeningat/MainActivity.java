package com.example.whatshappeningat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button submitButton;

    private EditText countryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        countryEditText = findViewById(R.id.countryEditText);


        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "SubmitButton clicked");
                openViewerActivity();
            }
        });

    }

    public void openViewerActivity() {
        Log.d(TAG, "openViewerActivity() running");
        // starts ViewerActivity with intent containing entered country name, does nothing if no country has been entered

        String countryString = countryEditText.getText().toString();

        if (Objects.equals(countryString, "Country")) {
            Toast.makeText(getApplicationContext(), "Please enter a country", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ViewerActivity.class);
        intent.putExtra("cityName", countryString);
        startActivity(intent);
    }
}