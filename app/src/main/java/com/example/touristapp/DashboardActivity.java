package com.example.touristapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    Button sosBtn, profileBtn, wearableBtn, mapBtn, firBtn, aiBtn;
    private SoundPool soundPool;
    private int sosSound;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isSOSActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 🔗 Link buttons with correct IDs from XML
        sosBtn = findViewById(R.id.btnSOS);
        profileBtn = findViewById(R.id.btnProfile);
        wearableBtn = findViewById(R.id.btnWearable);
        mapBtn = findViewById(R.id.btnMaps);
        firBtn = findViewById(R.id.btnFIR);
        aiBtn = findViewById(R.id.btnAI);

        // 🔊 SoundPool setup
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        // Load beep sound
        sosSound = soundPool.load(this, R.raw.sos_beep, 1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 🔴 SOS Button
        if (sosBtn != null) {
            sosBtn.setOnClickListener(v -> {
                Toast.makeText(this, "SOS Button Pressed", Toast.LENGTH_SHORT).show();
                soundPool.play(sosSound, 1f, 1f, 0, 0, 1f);

                if (checkPermissions()) {
                    sendSOSMessage();
                    startSOSLive();
                }
            });
        }

        // 👤 PROFILE BUTTON
        if (profileBtn != null) {
            profileBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // 🧳 Wearable
        if (wearableBtn != null) {
            wearableBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, WearableAndContactsActivity.class)));
        }

        // 🗺 Maps
        if (mapBtn != null) {
            mapBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, MapsActivity.class)));
        }

        // 📝 FIR
        if (firBtn != null) {
            firBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, FIRActivity.class)));
        }

        // 🤖 AI BUTTON
        if (aiBtn != null) {
            aiBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, AISystemActivity.class);
                startActivity(intent);
            });
        }
    }

    // 🔹 Fetch saved contacts
    private List<String> getSavedContacts() {
        SharedPreferences sp = getSharedPreferences("ContactsData", MODE_PRIVATE);
        List<String> contacts = new ArrayList<>();
        String[] phones = {
                sp.getString("phone1", ""),
                sp.getString("phone2", ""),
                sp.getString("phone3", ""),
                sp.getString("phone4", "")
        };
        for (String phone : phones) {
            if (phone != null && !phone.isEmpty()) {
                contacts.add(phone);
            }
        }
        return contacts;
    }

    // 🔹 Send SOS once
    private void sendSOSMessage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED 
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        String url = "https://maps.google.com/?q=" +
                                location.getLatitude() + "," + location.getLongitude();

                        for (String phone : getSavedContacts())
                            sendSMS(phone, "Emergency! My location: " + url);

                        Toast.makeText(this, "SOS sent to saved contacts!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to get location!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔹 Send SMS
    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (SecurityException e) {
            Toast.makeText(this, "SMS Permission denied", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Start live SOS updates
    private void startSOSLive() {
        isSOSActive = true;

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                if (!isSOSActive) return;

                Location loc = result.getLastLocation();
                if (loc != null) {
                    String url = "https://maps.google.com/?q=" +
                            loc.getLatitude() + "," + loc.getLongitude();

                    for (String phone : getSavedContacts())
                        sendSMS(phone, "Emergency! Live location: " + url);
                }
            }
        };

        // Using modern LocationRequest.Builder to avoid deprecations
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 30000)
                .setMinUpdateIntervalMillis(15000)
                .build();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.requestLocationUpdates(request, locationCallback, null);
        }
    }

    private void stopSOS() {
        isSOSActive = false;
        if (fusedLocationClient != null && locationCallback != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);

        Toast.makeText(this, "SOS deactivated", Toast.LENGTH_SHORT).show();
    }

    // 🔹 Permissions
    private boolean checkPermissions() {
        String[] perms = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        List<String> needed = new ArrayList<>();

        for (String p : perms) {
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                needed.add(p);
            }
        }

        if (!needed.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    needed.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] permissions,
                                           @NonNull int[] results) {
        super.onRequestPermissionsResult(code, permissions, results);

        if (code == PERMISSION_REQUEST_CODE) {
            boolean all = true;
            for (int r : results) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    all = false;
                    break;
                }
            }

            if (!all)
                Toast.makeText(this,
                        "Permissions are required for SOS!",
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSOS();

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}