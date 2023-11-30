package com.example.hams;



import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookAppointments extends AppCompatActivity {
    RecyclerView recyclerView;
    Button search;
    Spinner spinner;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    DatabaseReference usersRef = MainActivity.usersRef;
    DatabaseReference shiftRef = MainActivity.shiftRef;
    public static List<Appointment> bookableAppointmentList = new ArrayList<>();
    Query bookableDoctorsQuery;
    Query shiftQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.book_appointment_page);
        recyclerView = findViewById(R.id.recyclerview);
        search = findViewById(R.id.search);
        spinner = findViewById(R.id.spinnerSpecialties);
        Context context = this;

        //implement spinner functionality

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.specialties_array,
                android.R.layout.simple_spinner_item
        );
        //set arrayAdapter for the spinner values
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //actions when a spinner item is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedSpecialty = parentView.getItemAtPosition(position).toString();
                //specialty picked, assign string to pass into the search button
                Toast.makeText(getApplicationContext(), "Selected Specialty: " + selectedSpecialty, Toast.LENGTH_SHORT).show();
                //find the doctors with the matching specialty
                bookableDoctorsQuery = usersRef.orderByChild("specialties").equalTo(selectedSpecialty);
                bookableDoctorsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //now for each doctor, create an appointment for every time slot in each shift
                        bookableAppointmentList.clear();
                        for(DataSnapshot doctorSnapshot : snapshot.getChildren()){
                            Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                            //got all the shifts for that doctor
                            shiftQuery = shiftRef.orderByChild("doctorID").equalTo(doctor.getEmployeeNumber());
                            //now turn each shift into an appointment
                            shiftQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot shiftSnapshot : snapshot.getChildren()){
                                        Shift shift = shiftSnapshot.getValue(Shift.class);
                                        //now implement 30 min increments for the shifts
                                        //beginning of shift,
                                        LocalTime aStart = LocalTime.parse(shift.getStartTime());
                                        LocalTime aEnd;
                                        while(aStart.isBefore(LocalTime.parse(shift.getEndTime()))){
                                            aEnd = aStart.plusMinutes(30);
                                            //add the details of the appointment now
                                            //increment start and end at the end of the loop
                                            Appointment a = new Appointment();
                                            a.setDoctor(doctor);
                                            a.setDate(shift.getDate());
                                            a.setStartTime(aStart.toString());
                                            a.setEndTime(aEnd.toString());
                                            String appointmentId = appointmentsRef.push().getKey();
                                            a.setAppointmentID(appointmentId);
                                            appointmentsRef.child(appointmentId).setValue(a);
                                            Log.d("info","bookable appt created: "+a.getDate()+" "+a.getStartTime()+" with doctor: "+a.getDoctor().getEmployeeNumber());
                                            bookableAppointmentList.add(a);

                                            aStart = aEnd; //next appointment starts at the end of the previous one
                                        }

                                    }
                                    Log.d("info","bookable appointments size: "+bookableAppointmentList.size());
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(new BookAppointmentAdapter(getApplicationContext()));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
        /*for(int i = 1; i<6; i++){
            Appointment testA = new Appointment();
            testA.setDate("2023-11-30");
            testA.setStartTime("08:00");
            Patient p = new Patient();
            p.setFirstName("Patient " + i);
            p.setHealthCard("1234567890");
            testA.setPatient(p);
            testA.setDoctorID("0000000000");
            testA.setPatientID(p.getHealthCard());
            testA.setStatus("Approved");
            String appointmentId = appointmentsRef.push().getKey();
            testA.setAppointmentID(appointmentId);

            appointmentsRef.child(appointmentId).setValue(testA);

            approvedAppointmentList.add(testA);
            Log.d("Info", "appointment added: " + i);
        }*/


        /*
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

                if (isDateInPast(selectedDate)) {
                    Toast.makeText(UpcomingShifts.this, "Cannot add shift for a past date.", Toast.LENGTH_LONG).show();
                    return ;
                }

                if (isShiftConflicting(selectedDate, selectedStartTime, selectedEndTime)) {
                    Toast.makeText(UpcomingShifts.this, "Shift conflicts with an existing shift.", Toast.LENGTH_LONG).show();
                    return;
                }
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
        });*/
    }
}
