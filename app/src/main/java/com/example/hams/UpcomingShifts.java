package com.example.hams;


import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.List;

public class UpcomingShifts extends AppCompatActivity {
    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private RecyclerView recyclerView;
    private ShiftAdapter shiftAdapter;
    public static List<Shift> shiftList;
    Button new_shift;

    public void setSelectedDate(String date) {
        this.selectedDate = date;
    }
    public void setSelectedTime(String time, String tag) {
        if ("starttimePicker".equals(tag)) {
            this.selectedStartTime = time;
        } else if ("endtimePicker".equals(tag)) {
            this.selectedEndTime = time;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.upcoming_shifts_page);
        recyclerView = findViewById(R.id.recyclerview);
        new_shift = findViewById(R.id.new_shift);

        Context context = this;

        // Initialize your shiftList here with the data
        shiftList = new ArrayList<>();
        shiftList.add(new Shift("2023-11-25", "08:00", "16:00"));
        shiftList.add(new Shift("2023-11-26", "10:00", "18:00"));
        shiftList.add(new Shift("2023-11-27", "09:00", "17:00"));
        shiftList.add(new Shift("2023-11-28", "08:00", "16:00"));// Fetch or create your list of shifts


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ShiftAdapter(getApplicationContext(),shiftList));
        shiftAdapter = new ShiftAdapter(this, shiftList);
        recyclerView.setAdapter(shiftAdapter);
        findViewById(R.id.pickstartTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment().show(getSupportFragmentManager(), "starttimePicker");
            }
        });
        findViewById(R.id.pickendTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment().show(getSupportFragmentManager(), "endtimePicker");
            }
        });

        findViewById(R.id.pickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        new_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Shift newShift = new Shift(selectedDate, selectedStartTime, selectedEndTime);
                shiftList.add(newShift);
                shiftAdapter.notifyDataSetChanged();
            }
        });

    }
}



