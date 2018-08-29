package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kisanhubandroidassignment.data.WeatherDataHelper;

public class ShowWeatherActivity extends AppCompatActivity {

    WeatherDataHelper weatherDataHelper = new WeatherDataHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);


//        get the intent that started this activity and extract the string
        Intent intent = getIntent();

        String message = intent.getStringExtra(SplashActivity.EXTRA_WEATHER_DATA);
        displayDataDump(message);

        populateSpinner(R.id.regions_spinner, R.array.regions_array);
        populateSpinner(R.id.parameters_spinner, R.array.parameters_array);
    }


    private void populateSpinner(int spinnerId, int arrayId) {
        Spinner spinner = (Spinner) findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void displayDataDump(String message) {

//        capture the layout's text view and set the string as its text
        TextView textView = (TextView) findViewById(R.id.weather_data_text_view);
        textView.setText(message);


        Cursor data = weatherDataHelper.getWeatherData();
        data.move(1);


        int textViewCount = data.getCount();

        TextView[] textViewArray = new TextView[textViewCount];

        for (int i = 0; i < textViewCount; i++) {
            textViewArray[i] = new TextView(this);
        }


        TextView textView1 = (TextView) findViewById(R.id.weather_data_text_view1);
        textView1.setText("" + textViewCount);
    }

//    public void showWeatherForSelectedFilters(View view) {
//        weatherDataHelper.getWeatherDataForRegionAndParameter(region, parameter);
//
//
//    }
}
