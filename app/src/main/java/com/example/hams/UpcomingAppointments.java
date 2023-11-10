package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UpcomingAppointments extends AppCompatActivity {
    RecyclerView recyclerViewPending;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pending_appointment_page);

        //recyclerViewPending = findViewById(R.id.recyclerviewUpcoming);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Log.d("Info", "item id/page: " + item.getItemId());
            Log.d("Info", "item ID printing?");
            switch (item.getItemId()) {
                case 2131362086: //int value of the R.id.menu_approved
                    // Load "Approved" users
                    Log.d("Info", "switch to approved appointments");
                    startActivity(new Intent(UpcomingAppointments.this, ApprovedAppointments.class));
                    return true;
                case 2131362087: //pending view
                    Log.d("Info", "switch back to pending appointments");
                    return true;
                default:
                    Log.d("Info", "DEFAULTINGGGG");
                    return false;
            }
        });
    }
}
