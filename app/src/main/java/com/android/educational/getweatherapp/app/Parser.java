package com.android.educational.getweatherapp.app;

import com.android.educational.getweatherapp.app.data.WeatherInfo;
import com.android.educational.getweatherapp.app.data.PlaceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

    public static WeatherInfo getWeather(String data) throws JSONException  {
        WeatherInfo weatherInfo = new WeatherInfo();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        PlaceInfo loc = new PlaceInfo();

        weatherInfo.setDayTime(getInt("dt", jObj));

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weatherInfo.placeInfo = loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weatherInfo.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weatherInfo.currentCondition.setDescr(getString("description", JSONWeather));
        weatherInfo.currentCondition.setCondition(getString("main", JSONWeather));
        weatherInfo.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weatherInfo.currentCondition.setHumidity(getInt("humidity", mainObj));
        weatherInfo.currentCondition.setPressure(getInt("pressure", mainObj));
        weatherInfo.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weatherInfo.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weatherInfo.temperature.setTemp(getFloat("temp", mainObj));

        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weatherInfo.wind.setSpeed(getFloat("speed", wObj));
        weatherInfo.wind.setDeg(getFloat("deg", wObj));

        // Clouds
        JSONObject cObj = getObject("clouds", jObj);
        weatherInfo.clouds.setPerc(getInt("all", cObj));

        // We download the icon to show


        return weatherInfo;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
