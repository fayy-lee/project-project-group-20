package com.example.hams;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class rateAppointments extends AppCompatActivity {

    TextView rateCount, showRating;
    Button submit;
    RatingBar ratingBar;
    float rateValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_past_appointments);

        rateCount = findViewById(R.id.rateCount);
        ratingBar = findViewById(R.id.ratingBar);
        submit = findViewById(R.id.submitBtn);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                if (rateValue <= 1 && rateValue > 0)
                    rateCount.setText("Bad" + rateValue + "/5");
                else if (rateValue <= 2 && rateValue > 0)
                    rateCount.setText("OK" + rateValue + "/5");
                else if (rateValue <= 3 && rateValue > 0)
                    rateCount.setText("Good" + rateValue + "/5");
                else if (rateValue <= 4 && rateValue > 0)
                    rateCount.setText("Very Good" + rateValue + "/5");
                else if (rateValue <= 5 && rateValue > 0)
                    rateCount.setText("Perfect" + rateValue + "/5");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(0);
                rateCount.setText("");
            }
        });
    }
}