package com.example.hams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ApprovedAppointments extends AppCompatActivity {
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    FirebaseAuth mAuth;
    DatabaseReference usersRef = MainActivity.usersRef;
    List<Appointment> upcomingAppointmentList = UpcomingAppointments.upcomingAppointmentList;
    List<Appointment> approvedAppointmentList = UpcomingAppointments.approvedAppointmentList;

    Query approvedAppointmentsQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.approved_appointment_page);
        Context context = this;
        RecyclerView recyclerViewApproved = findViewById(R.id.recyclerviewApproved);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //currently signed in user
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference userRef = usersRef.child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //query to get the appointments associated with the right doctor
                String employeeNumber = snapshot.child("employeeNumber").getValue(String.class);
                Log.d("Info","DOCTOR NAME: "+employeeNumber);
                approvedAppointmentsQuery = appointmentsRef.orderByChild("doctorID").equalTo(employeeNumber);
                //now read from the query data
                approvedAppointmentsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        approvedAppointmentList.clear();
                        // Iterate through the dataSnapshot to get the appointments
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if(appointment != null){
                                if(appointment.getStatus().equals("Approved")){
                                    approvedAppointmentList.add(appointment);
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                recyclerViewApproved.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewApproved.setAdapter(new ApprovedAppointmentAdapter(getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(listener ->{
            Log.d("info","item id clicked: "+listener.getItemId());
            if(listener.getItemId() == R.id.menu_pending){
                //swtiched to approved appointments page
                Intent intent = new Intent(ApprovedAppointments.this, UpcomingAppointments.class);
                startActivity(intent);
                return true;
            } else{
                return true;
            }
        });
    }
    }

