package com.example.touristapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etAadhar, etPassword, etConfirmPassword;
    Button btnSignUp;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 🔗 Link XML fields
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAadhar = findViewById(R.id.etAadhar);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        // 🔥 SIGNUP BUTTON CLICK
        btnSignUp.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String aadhar = etAadhar.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // 🔴 Validation
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                    || aadhar.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
                return;
            }

            // 💾 SAVE DATA (IMPORTANT)
            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("name", name);
            editor.putString("email", email);
            editor.putString("phone", phone);
            editor.putString("aadhar", aadhar);

            editor.apply();

            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();

            // 🚀 Move to next screen (Dashboard or Login)
            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // 🔹 Login redirect
        tvLogin.setOnClickListener(v -> {
            finish(); // back to login
        });
    }
}