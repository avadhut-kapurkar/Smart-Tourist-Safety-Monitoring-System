package com.example.touristapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;

    // 🔥 UI elements
    Button btnShareLocation;
    TextView txtStatus;

    // 🔥 Location
    private FusedLocationProviderClient fusedLocationClient;

    // 🔥 Geofencing
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // UI connect
        btnShareLocation = findViewById(R.id.btnShareLocation);
        txtStatus = findViewById(R.id.txtStatus);

        // Location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Geofencing client
        geofencingClient = LocationServices.getGeofencingClient(this);

        checkLocationPermission();

        // 🔵 Share Location button
        btnShareLocation.setOnClickListener(v -> shareLocation());
    }

    private void shareLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {

            if (location != null) {

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                LatLng userLocation = new LatLng(lat, lng);

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                txtStatus.setText("Location Shared ✅");

            } else {
                txtStatus.setText("Location not found ❌");
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setupMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();

                Toast.makeText(this, "Enable 'Allow all the time' for Geo-fencing", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);

            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng shirala = new LatLng(16.8556, 74.5656);
        mMap.addMarker(new MarkerOptions().position(shirala).title("Marker in Sangli"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shirala, 12));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Geofence
        addGeofence(shirala);
    }

    private void addGeofence(LatLng latLng) {

        Geofence geofence = new Geofence.Builder()
                .setRequestId("MY_GEOFENCE_ID")
                .setCircularRegion(latLng.latitude, latLng.longitude, 200)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT
                )
                .build();

        GeofencingRequest request = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        geofencingClient.addGeofences(request, getGeofencePendingIntent());
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Intent intent = new Intent(this, GeoFenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return geofencePendingIntent;
    }
}