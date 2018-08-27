package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);


//        get the intent that started this activity and extract the string
        Intent intent = getIntent();

        String message = intent.getStringExtra(SplashActivity.EXTRA_WEATHER_DATA);
        displayDataDump(message);
    }

    private void displayDataDump(String message) {

//        capture the layout's text view and set the string as its text
        TextView textView = (TextView) findViewById(R.id.weather_data_text_view);
        textView.setText(message);
    }
}
