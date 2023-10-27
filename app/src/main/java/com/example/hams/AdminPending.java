package com.example.hams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminPending extends AppCompatActivity {
    private TextView textView;
    private ImageButton reject, accept;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending);

        textView = findViewById(R.id.name);
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int destinationId = item.getItemId();

            // Save the selected destination to SharedPreferences
            sharedPreferences.edit().putInt("selected_destination", destinationId).apply();

            return true;
        });

        // Initialize the selected destination from SharedPreferences
        int selectedDestination = sharedPreferences.getInt("selected_destination", R.id.navigation_main);
        bottomNavigationView.setSelectedItemId(selectedDestination);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display text
                Toast.makeText(AdminPending.this, "User Rejected", Toast.LENGTH_SHORT).show();

                // get text from TextView
                String text = textView.getText().toString();

                // Save the data in SharedPreferences
                sharedPreferences.edit().putString("data_to_transfer", text).apply();
                bottomNavigationView.setSelectedItemId(R.id.nav_approved);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display text
                Toast.makeText(AdminPending.this, "User Accepted", Toast.LENGTH_SHORT).show();

                // get text from TextView
                String text = textView.getText().toString();

                // Save the data in SharedPreferences
                sharedPreferences.edit().putString("data_to_transfer", text).apply();
                bottomNavigationView.setSelectedItemId(R.id.nav_rejects);
            }
        });
    }
}
