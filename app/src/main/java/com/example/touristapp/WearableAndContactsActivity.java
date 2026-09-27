package com.example.touristapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WearableAndContactsActivity extends AppCompatActivity {

    private EditText name1, phone1, name2, phone2, name3, phone3, name4, phone4;
    private Button btnSaveContacts, btnConnectWearable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_and_contacts);

        // 🔗 UI connect
        name1 = findViewById(R.id.name1);
        phone1 = findViewById(R.id.phone1);
        name2 = findViewById(R.id.name2);
        phone2 = findViewById(R.id.phone2);
        name3 = findViewById(R.id.name3);
        phone3 = findViewById(R.id.phone3);
        name4 = findViewById(R.id.name4);
        phone4 = findViewById(R.id.phone4);
        btnSaveContacts = findViewById(R.id.btnSaveContacts);
        btnConnectWearable = findViewById(R.id.btnConnectWearable);

        // Load existing data
        loadContacts();

        // 💾 Save button click
        btnSaveContacts.setOnClickListener(v -> saveContacts());

        // 🔵 Connect wearable
        btnConnectWearable.setOnClickListener(v -> {
            Toast.makeText(this, "Searching for wearable devices...", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveContacts() {
        SharedPreferences sp = getSharedPreferences("ContactsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("name1", name1.getText().toString().trim());
        editor.putString("phone1", phone1.getText().toString().trim());
        editor.putString("name2", name2.getText().toString().trim());
        editor.putString("phone2", phone2.getText().toString().trim());
        editor.putString("name3", name3.getText().toString().trim());
        editor.putString("phone3", phone3.getText().toString().trim());
        editor.putString("name4", name4.getText().toString().trim());
        editor.putString("phone4", phone4.getText().toString().trim());

        editor.apply();
        Toast.makeText(this, "Contacts saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadContacts() {
        SharedPreferences sp = getSharedPreferences("ContactsData", MODE_PRIVATE);
        name1.setText(sp.getString("name1", ""));
        phone1.setText(sp.getString("phone1", ""));
        name2.setText(sp.getString("name2", ""));
        phone2.setText(sp.getString("phone2", ""));
        name3.setText(sp.getString("name3", ""));
        phone3.setText(sp.getString("phone3", ""));
        name4.setText(sp.getString("name4", ""));
        phone4.setText(sp.getString("phone4", ""));
    }
}
