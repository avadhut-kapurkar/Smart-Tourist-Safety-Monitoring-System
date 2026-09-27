package com.example.touristapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FIRActivity extends AppCompatActivity {

    Button btnFIR, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir);

        // Connect buttons
        btnFIR = findViewById(R.id.btnFIR);
        btnBack = findViewById(R.id.btnBack);

        // 🔵 FIR Button → Maharashtra Police website
        btnFIR.setOnClickListener(v -> {
            String url = "https://www.mahapolice.gov.in/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        // 🔙 Back button → Dashboard
        btnBack.setOnClickListener(v -> {
            finish(); // go back
        });
    }
}