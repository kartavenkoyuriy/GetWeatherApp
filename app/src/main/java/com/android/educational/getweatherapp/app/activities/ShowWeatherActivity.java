package com.android.educational.getweatherapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.educational.getweatherapp.app.ConnectionClient;
import com.android.educational.getweatherapp.app.Parser;
import com.android.educational.getweatherapp.app.R;
import com.android.educational.getweatherapp.app.data.WeatherInfo;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ShowWeatherActivity extends Activity {

    private TextView userTextInput;

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private TextView hum;
    private ImageView imgView;
    private TextView longitude;
    private TextView latitude;
    private TextView sunrise;
    private TextView sunset;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView dayTime;

    public String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weather);
        city = getIntent().getStringExtra("cityName");
        userTextInput = (TextView) findViewById(R.id.userTextInput);
        userTextInput.setText("Your input: " + city);

        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condValue);
        temp = (TextView) findViewById(R.id.tempValue);
        hum = (TextView) findViewById(R.id.humValue);
        press = (TextView) findViewById(R.id.pressValue);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);
        latitude = (TextView) findViewById(R.id.latitudeValue);
        longitude = (TextView) findViewById(R.id.longitudeValue);
        sunrise = (TextView) findViewById(R.id.sunriseValue);
        sunset = (TextView) findViewById(R.id.sunsetValue);
        maxTemp = (TextView) findViewById(R.id.maxTempValue);
        minTemp = (TextView) findViewById(R.id.minTempValue);
        dayTime = (TextView) findViewById(R.id.dayTimeValue);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
    }


    public void backToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToMap(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("city", cityText.getText().toString());
        intent.putExtra("longitude", longitude.getText().toString());
        intent.putExtra("latitude", latitude.getText().toString());
        startActivity(intent);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, WeatherInfo> {

        @Override
        protected WeatherInfo doInBackground(String... params) {
            WeatherInfo weather = new WeatherInfo();
            String data = ((new ConnectionClient()).getWeatherData(params[0]));

            try {
                weather = Parser.getWeather(data);
                Log.d("--***** MAP  ", "::before retrieve the icon");
                // Let's retrieve the icon
                weather.iconData = ((new ConnectionClient()).getImage(weather.currentCondition.getIcon()));
                Log.d("--***** MAP  ", "::after retrieve the icon. weather.iconData.length = " + weather.iconData.length);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(WeatherInfo weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Log.d("--***** MAP  ", "::check iconData complete");
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                Log.d("--***** MAP  ", "::decode the img - isNull? " + (img == null));
                imgView.setImageBitmap(img);
            }
            Log.d("--***** MAP  ", "::after decode the img");

            cityText.setText(weather.placeInfo.getCity() + ", " + weather.placeInfo.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "\u00B0C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "\u00B0");
            longitude.setText("" + weather.placeInfo.getLongitude());
            latitude.setText("" + weather.placeInfo.getLatitude());
            maxTemp.setText("" + Math.round((weather.temperature.getMaxTemp() - 273.15)) + "\u00B0C");
            minTemp.setText("" + Math.round((weather.temperature.getMinTemp() - 273.15)) + "\u00B0C");

            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date sunriseDate = new Date(weather.placeInfo.getSunrise()*1000);
            String sunriseText = formatter.format(sunriseDate);
            Date sunsetDate = new Date(weather.placeInfo.getSunset()*1000);
            String sunsetText = formatter.format(sunsetDate);
            Date dayTimeDate = new Date(weather.getDayTime()*1000);
            String dayTimeText = formatter.format(dayTimeDate);
            sunrise.setText(sunriseText);
            sunset.setText(sunsetText);
            dayTime.setText(dayTimeText);
        }

    }
}