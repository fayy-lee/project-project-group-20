package com.example.hams;


import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        shiftList = new ArrayList<>();
        shiftAdapter = new ShiftAdapter(this, shiftList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shiftAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Shifts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                shiftList.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shift shift = snapshot.getValue(Shift.class);
                    if (shift != null) {
                        shift.setShiftID(snapshot.getKey());
                        shiftList.add(shift);
                    }
                }
                shiftAdapter.notifyDataSetChanged(); // Refresh RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });


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
                // Check if the date, start time, and end time are not null
                if (selectedDate != null && selectedStartTime != null && selectedEndTime != null) {
                    // All fields are set, proceed to create a new shift
                    Shift newShift = new Shift(selectedDate, selectedStartTime, selectedEndTime);
                    DatabaseReference shiftsRef = FirebaseDatabase.getInstance().getReference().child("Shifts");

                    // Generate a unique key for the new shift
                    String shiftId = shiftsRef.push().getKey();
                    newShift.setShiftID(shiftId);
                    shiftsRef.child(shiftId).setValue(newShift).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully written to Firebase, the ValueEventListener will update the UI
                            Log.d("Firebase", "Shift added successfully");
                        } else {
                            // Handle failure
                            Log.e("Firebase", "Failed to add shift", task.getException());
                        }
                    });


                } else {
                    // One or more fields are null, handle this case appropriately
                    Log.e("NewShiftError", "Cannot add shift: Date, start time, or end time is missing");
                    // Optionally, show a user-friendly message or prompt to input missing data
                }
            }
        });
    }

}



