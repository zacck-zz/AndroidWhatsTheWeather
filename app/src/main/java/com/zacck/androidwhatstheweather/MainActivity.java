package com.zacck.androidwhatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvWeather;
    EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWeather = (TextView)findViewById(R.id.tvWeather);
        etCity = (EditText)findViewById(R.id.etMcity);




    }

    public void getWeather(View view)
    {
        if(etCity.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Enter a city for the App to work", Toast.LENGTH_LONG).show();
        }
        else {
            InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(etCity.getWindowToken(),0);
            //make an instance of downloadweather
            DownloadWeather mdoDownloadWeather = new DownloadWeather();
            try {

                mdoDownloadWeather.execute("http://api.openweathermap.org/data/2.5/weather?q=" + etCity.getText().toString() + ",uk&appid=44db6a862fba0b067b1930da0d769e98").get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadWeather extends AsyncTask<String, Void, String>
    {
        String result = "";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject mWeatherObject = new JSONObject(result);
                String mWeatherContent = mWeatherObject.getString("weather");

                //loop through the info
                JSONArray mWeatherArray = new JSONArray(mWeatherContent);
                for(int i=0; i<mWeatherArray.length(); i++)
                {
                    JSONObject mWeatherChild = mWeatherArray.getJSONObject(i);

                    String mMain = mWeatherChild.getString("main");
                    String mDesc = mWeatherChild.getString("description");

                    tvWeather.setText(mMain +" : "+mDesc);



                }
            }
            catch(Exception e)
            {
                tvWeather.setText(e.toString());
            }
        }

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
