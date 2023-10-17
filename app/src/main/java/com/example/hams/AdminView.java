package com.example.hams;

import static com.example.hams.MainActivity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminView extends AppCompatActivity {
    CardView logoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_view);
        //defining the cards (from dahsboards.xml)
        logoutCard = findViewById(R.id.logout);
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.logout();
                startActivity(new Intent(AdminView.this, MainActivity.class));
            }
        });
    }
}