package com.android.educational.getweatherapp.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.android.educational.getweatherapp.app.R;


public class MainActivity extends Activity {

    private EditText editCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCity = (EditText) findViewById(R.id.editCity);
    }

    public void sendCityName(View v){
        Intent intent = new Intent(this, ShowWeatherActivity.class);
        intent.putExtra("cityName", editCity.getText().toString());
        startActivity(intent);
    }


}
