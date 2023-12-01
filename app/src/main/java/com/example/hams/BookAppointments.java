package com.example.hams;



import static com.example.hams.MainActivity.appointmentsRef;

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
                                        while(aStart.isBefore(java.time.LocalTime.parse(shift.getEndTime()))){
                                            aEnd = aStart.plusMinutes(30);
                                            //add the details of the appointment now
                                            //increment start and end at the end of the loop
                                            Appointment a = new Appointment();
                                            a.setDoctor(doctor);
                                            a.setDate(shift.getDate());
                                            a.setStartTime(aStart.toString());
                                            a.setEndTime(aEnd.toString());
                                            a.setPatient(new Patient());
                                            String appointmentId = appointmentsRef.push().getKey();
                                            a.setAppointmentID(appointmentId);
                                            a.setStatus("Not Booked");
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
    }
}
