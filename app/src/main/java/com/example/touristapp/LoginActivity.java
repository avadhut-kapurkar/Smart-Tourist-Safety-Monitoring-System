package com.example.touristapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button signupBtn = findViewById(R.id.signupBtn);

        // Login -> Dashboard
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        // Signup -> SignupActivity
        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}