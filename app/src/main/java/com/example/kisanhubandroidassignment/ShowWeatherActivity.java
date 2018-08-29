package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        showDataLoadedMessage(message);

        Spinner regionSpinner = (Spinner) findViewById(R.id.regions_spinner);
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object selectedItem = parent.getItemAtPosition(position);
                String message = selectedItem.toString();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                showDataForFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void showDataForFilters() {

        int count = weatherDataHelper.getWeatherDataCount();

//        TextView[] textViewArray = new TextView[count];
//
//        for (int i = 0; i < count; i++) {
//            textViewArray[i] = new TextView(this);
//        }


        TextView textView1 = (TextView) findViewById(R.id.weather_data_text_view1);
        textView1.setText(getString(R.string.no_of_rows) + count);
    }

    private void showDataLoadedMessage(String message) {

//        capture the layout's text view and set the string as its text
        TextView textView = (TextView) findViewById(R.id.weather_data_text_view);
        textView.setText(message);

    }


}
