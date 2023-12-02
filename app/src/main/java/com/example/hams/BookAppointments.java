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
                        if(!snapshot.exists()){
                            Log.d("info","no doctors with the specialty: "+selectedSpecialty);
                        }else {
                            //now for each doctor, create an appointment for every time slot in each shift
                            bookableAppointmentList.clear();

                            for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                                Doctor doctor = doctorSnapshot.getValue(Doctor.class);
                                //got all the shifts for that doctor
                                shiftQuery = shiftRef.orderByChild("doctorID").equalTo(doctor.getEmployeeNumber());
                                //now turn each shift into an appointment
                                shiftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.d("info","shift snap size: "+snapshot.getChildrenCount());

                                        for (DataSnapshot shiftSnapshot : snapshot.getChildren()) {

                                            Shift shift = shiftSnapshot.getValue(Shift.class);
                                            for(Appointment a : shift.getShiftAppointments()){
                                                if(a.getStatus().equals("Not Booked")){
                                                    bookableAppointmentList.add(a);
                                                }
                                            }
                                        }
                                        Log.d("info", "bookable appointments size: " + bookableAppointmentList.size());
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                        recyclerView.setAdapter(new BookAppointmentAdapter(getApplicationContext()));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
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
