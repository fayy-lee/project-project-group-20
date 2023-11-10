package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

public class ApprovedAppointments extends AppCompatActivity {
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.approved_appointment_page);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Log.d("Info", "item id/page: " + item.getItemId());
            Log.d("Info", "item ID printing?");
            switch (item.getItemId()) {
                case 2131362086: //int value of the R.id.menu_approved
                    // Load "Approved" users
                    Log.d("Info", "switch to upcoming appointments");
                    startActivity(new Intent(ApprovedAppointments.this, UpcomingAppointments.class));
                    return true;
                case 2131362087: //pending view
                    Log.d("Info", "switch back to approved appointments");
                    return true;
                default:
                    Log.d("Info", "DEFAULTINGGGG");
                    return false;
            }
        });
    }
    }

