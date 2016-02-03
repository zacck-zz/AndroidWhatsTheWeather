package com.zacck.androidwhatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make an instance of downloadweather
        DownloadWeather mdoDownloadWeather = new DownloadWeather();
        try {
            String returned = mdoDownloadWeather.execute("http://api.openweathermap.org/data/2.5/weather?q=Johannesburg,uk&appid=44db6a862fba0b067b1930da0d769e98").get();
            Log.i(getPackageName(), returned);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public class DownloadWeather extends AsyncTask<String, Void, String>
    {
        String result = "";

        @Override
        protected String doInBackground(String... weatherUrls) {
            try
            {
                //lets make the link a url
                URL mWeatherUrl = new URL(weatherUrls[0]);
                //lets connect to the url
                HttpURLConnection mWeatherConnection = (HttpURLConnection)mWeatherUrl.openConnection();
                mWeatherConnection.connect();
                //let us get teh goods flow of informations from the internet
                InputStream mWeatherInputStream = mWeatherConnection.getInputStream();

                InputStreamReader mInputReader = new InputStreamReader(mWeatherInputStream);
                int data = mInputReader.read();
                while(data != -1)
                {
                    //convert the current byte into a character and append it to our result string
                    char currentByte = (char)data;
                    result += currentByte;

                    //keep the loog going
                    data = mInputReader.read();
                }
                return  result;

            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }


        }
    }
}
