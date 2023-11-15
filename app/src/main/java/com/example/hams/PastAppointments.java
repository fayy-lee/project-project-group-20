package com.example.hams;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PastAppointments extends AppCompatActivity {

    List<Appointment> pastAppointmentList = UpcomingAppointments.pastAppointmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.past_appointment_page);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
    }
}
