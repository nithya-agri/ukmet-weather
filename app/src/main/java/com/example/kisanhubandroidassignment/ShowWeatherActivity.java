package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kisanhubandroidassignment.data.WeatherDataHelper;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import static com.example.kisanhubandroidassignment.data.WeatherDataHelper.MONTH_COLUMN_NAME;
import static com.example.kisanhubandroidassignment.data.WeatherDataHelper.VALUE_COLUMN_NAME;

public class ShowWeatherActivity extends AppCompatActivity {

    WeatherDataHelper weatherDataHelper = new WeatherDataHelper(this);
    private String selectedRegion = "";
    private String selectedParameter = "";
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);


//        get the intent that started this activity and extract the string
        Intent intent = getIntent();

        String message = intent.getStringExtra(SplashActivity.EXTRA_WEATHER_DATA);
        showDataLoadedMessage(message);

        Spinner regionSpinner = findViewById(R.id.regions_spinner);
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


        Spinner parameterSpinner = findViewById(R.id.parameters_spinner);
        parameterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showDataForFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        ScrollableNumberPicker yearPicker = findViewById(R.id.year_picker);
        yearPicker.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                showDataForFilters();
            }
        });
    }


    private void showDataForFilters() {

        Spinner regionSpinner = findViewById(R.id.regions_spinner);
        String region = regionSpinner.getSelectedItem().toString();
        Spinner parameterSpinner = findViewById(R.id.parameters_spinner);
        String parameter = parameterSpinner.getSelectedItem().toString();
        ScrollableNumberPicker yearPicker = findViewById(R.id.year_picker);
        int year = yearPicker.getValue();
        Toast.makeText(getBaseContext(), region + " - " + parameter + " - " + year, Toast.LENGTH_LONG).show();

        if (!selectedRegion.equals(region) || !selectedParameter.equals(parameter) || !(selectedYear == year)) {
            selectedRegion = region;
            selectedParameter = parameter;
            selectedYear = year;
            Cursor cursor = weatherDataHelper.getWeatherDataForRegionParameterAndYear(region, parameter, year);
            TextView dataCountView = findViewById(R.id.weather_data_count_view);
            dataCountView.setText(getString(R.string.no_of_rows) + cursor.getCount());

            ListView listView = findViewById(R.id.weather_data_list_view);

            String[] columns = {
                    MONTH_COLUMN_NAME,
                    VALUE_COLUMN_NAME
            };
            int[] resourceIds = {
                    R.id.month_text_view,
                    R.id.value_text_view
            };

            ListAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.weather_data_row, cursor, columns, resourceIds, 0);
            listView.setAdapter(listAdapter);

        }

    }

    private void showDataLoadedMessage(String message) {

//        capture the layout's text view and set the string as its text
        TextView textView = findViewById(R.id.weather_data_message_view);
        textView.setText(message);

    }


}
