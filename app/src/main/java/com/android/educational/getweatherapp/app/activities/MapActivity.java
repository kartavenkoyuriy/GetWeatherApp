package com.android.educational.getweatherapp.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.android.educational.getweatherapp.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private double longitude;
    private double latitude;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        cityName = getIntent().getStringExtra("city");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Log.d("--***** MAP  ", "::before getMapAsync");

        mapFragment.getMapAsync(this);
        Log.d("--***** MAP  ", "::after getMapAsync");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("--***** MAP  ", "::onMapReady start");

        LatLng cityCoord = new LatLng(latitude, longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityCoord, 13));

        map.addMarker(new MarkerOptions()
                .title(cityName)
                .position(cityCoord));
        Log.d("--***** MAP  ", "::onMapReady end");
    }
}

