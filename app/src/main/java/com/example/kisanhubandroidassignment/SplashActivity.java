package com.example.kisanhubandroidassignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.kisanhubandroidassignment.data.WeatherDataHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void showWeatherData(String response) {
        if (response != null) {
            Intent intent = new Intent(this, ShowWeatherActivity.class);
            intent.putExtra(EXTRA_WEATHER_DATA, response);
            startActivity(intent);
        } else {
            toastMessage("Please check connection!");
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
                    // Create URL object
                    URL url = createUrl("https://www.metoffice.gov.uk/pub/data/weather/uk/climate/datasets/" + parameter + "/date/" + region + ".txt");

                    // Perform HTTP request to the URL and receive a JSON response back
                    String jsonResponse = "";
                    try {
                        jsonResponse = makeHttpRequest(url);
                    } catch (IOException e) {
                        // TODO Handle the IOException
                    }
                }
            }

            return "Success";
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
        private String makeHttpRequest(URL url) throws IOException {
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
                jsonResponse = readFromStream(inputStream);
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
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                int lineNo = 1;
                while (line != null) {

                    if (lineNo <= 8) {
                        lineNo++;
                        line = reader.readLine();
                        continue;
                    }
                    String[] lineElements = line.split("\\s+");
                    String year = lineElements[0];
                    String region = "UK";
                    String parameter = "Sunshine";
                    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Win", "Spr", "Sum", "Aut", "Ann"};

                    for (int i = 1; i < lineElements.length; i++) {
                        String month = months[i - 1];

                        String value = lineElements[i];
                        if (value.equals("---")) {
                            continue;
                        }
                        weatherDataHelper.writeWeatherData(region, parameter, Integer.parseInt(year), month, Double.parseDouble(value));
                    }
                    output.append(line);
                    lineNo++;
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }
}
