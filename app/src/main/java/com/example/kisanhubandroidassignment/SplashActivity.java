package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.kisanhubandroidassignment.data.WeatherDataHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class SplashActivity extends AppCompatActivity {

    private static final String[] REGIONS = {"UK", "England", "Wales", "Scotland"};
    private static final String[] PARAMETERS = {"Rainfall", "Tmin", "Tmean", "Tmax", "Sunshine"};

    WeatherDataHelper weatherDataHelper = new WeatherDataHelper(this);

    static final String EXTRA_WEATHER_DATA = "com.example.kisanhubandroidassignment.WEATHER_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        WeatherLoadingTask task = new WeatherLoadingTask();
        task.execute();
    }

    public void loadData(String response) {
//        parse data and load it

        addData("hello");

        showWeatherData(response);
    }

    public void showWeatherData(String response) {
        if (response != null) {
            Intent intent = new Intent(this, ShowWeatherActivity.class);
            intent.putExtra(EXTRA_WEATHER_DATA, response);
            startActivity(intent);
        } else {
            toastMessage("Please check connection!");
        }
    }

    public void addData(String newEntry) {
        boolean insertData = weatherDataHelper.addData(newEntry);
        if (insertData) {
            toastMessage("Data added");
        } else {
            toastMessage("Not added");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private class WeatherLoadingTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            for (String region : REGIONS) {
                for (String parameter : PARAMETERS) {
                    String urlString = "https://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/" + parameter + "/date/" + region + ".txt";
                    // Create URL object
                    URL url = createUrl(urlString);

                    // Perform HTTP request to the URL and receive a JSON response back
                    InputStream inputStream;
                    try {
                        inputStream = makeHttpRequest(url);
                        readFromStreamAndWrite(inputStream);
                    } catch (IOException e) {
                        // TODO Handle the IOException
                    }
                }
            }


            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            showWeatherData(response);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            try {
                return new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private InputStream makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return inputStream;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private void readFromStreamAndWrite(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                String region = "UK";
                String parameter = "Rainfall";

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                LineNumberReader reader = new LineNumberReader(inputStreamReader);
                String line = reader.readLine();

                while (line != null) {
                    if (reader.getLineNumber() <= 8) {
                        line = reader.readLine();
                        continue;
                    }
                    String[] lineElements = line.split("\\s+");
                    String year = lineElements[0];
                    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Win", "Spr", "Sum", "Aut", "Ann"};

                    for (int i = 1; i <= lineElements.length; i++) {
                        String month = months[i - 1];
                        String value = lineElements[i];
                    }
                    line = reader.readLine();
                }
            }
        }
    }
}
