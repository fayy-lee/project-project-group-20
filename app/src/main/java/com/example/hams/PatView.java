package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PatView extends AppCompatActivity {
    CardView logoutCard, upcomingCard, pastCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pat_view);
        //defining the cards (from dahsboards.xml)
        logoutCard = findViewById(R.id.logout);
        upcomingCard = findViewById(R.id.upcoming);
        pastCard = findViewById(R.id.past);
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatView.this, MainActivity.class));
            }
        });
        upcomingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatView.this, UpcomingPatientAppointments.class));
            }
        });

        pastCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatView.this, PastAppointments.class));
            }
        });
    }
}