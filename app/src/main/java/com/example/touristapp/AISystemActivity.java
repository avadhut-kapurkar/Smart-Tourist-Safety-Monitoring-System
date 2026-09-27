package com.example.touristapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AISystemActivity extends AppCompatActivity {

    Button btnGemini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_system);

        btnGemini = findViewById(R.id.btnGemini);

        btnGemini.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://gemini.google.com/"));
            startActivity(intent);
        });
    }
}
