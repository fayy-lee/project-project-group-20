package com.example.hams;

import static com.example.hams.R.id.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DocView extends AppCompatActivity {
    CardView logoutCard, shiftsCard, upcomingCard, pastCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doc_view);
        //defining the cards (from dahsboards.xml)
        logoutCard = findViewById(R.id.logout);
        shiftsCard = findViewById(R.id.shifts);
        upcomingCard = findViewById(R.id.upcoming);
        pastCard = findViewById(R.id.past);


        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocView.this, MainActivity.class));
            }
        });

        shiftsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocView.this, UpcomingShifts.class));
            }
        });

        upcomingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocView.this, UpcomingAppointments.class));
            }
        });

        pastCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocView.this, PastAppointments.class));
            }
        });
    }
}