package com.example.travel_app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    GoogleMap gMap;
    FrameLayout map;
    String title, description, country;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        map = findViewById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        title = i.getStringExtra("title");
        description = i.getStringExtra("description");
        country="";
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(36.8065, 10.1815);
        this.gMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        this.gMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(latLng));
        this.gMap.setOnMapClickListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println("onMapClick");

        double clickedLatitude = latLng.latitude;
        double clickedLongitude = latLng.longitude;

        try {
            Geocoder geocoder = new Geocoder(Maps.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(clickedLatitude, clickedLongitude, 1);
            country = addresses.get(0).getCountryName();
            Toast.makeText(this, "" + country , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // Handle the exception here
            Toast.makeText(this, "Error retrieving address information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        showConfirmationDialog(clickedLatitude, clickedLongitude, country);
    }

    private void showConfirmationDialog(double latitude, double longitude, String country) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to confirm the selected location?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Maps.this, "Location confirmed!"+country, Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("title", title);
                        resultIntent.putExtra("description", description);
                        resultIntent.putExtra("latitude", latitude);
                        resultIntent.putExtra("longitude", longitude);
                        resultIntent.putExtra("country", country);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
