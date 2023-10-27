package com.example.hams;

import android.content.Context;  // Import the Context class
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminApproved extends AppCompatActivity {

    private TextView receivedText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approved);

        receivedText = findViewById(R.id.textReceived);
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

        // Retrieve the data from SharedPreferences
        String text = sharedPreferences.getString("data_to_transfer", "No data");
        receivedText.setText(text);
    }
}
