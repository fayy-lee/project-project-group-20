package com.example.hams;
import android.widget.TextView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AdminApproved extends AppCompatActivity {

    private TextView receivedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approved);

        receivedText = findViewById(R.id.textReceived);

        // get text from intent
        String text = getIntent().getStringExtra("text");

        // display the text
        receivedText.setText(text);

    }
}