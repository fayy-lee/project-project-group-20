package com.example.hams;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class


public class UpcomingAppointments extends AppCompatActivity {

    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    DatabaseReference usersRef = MainActivity.usersRef;
    public static List<Appointment> upcomingAppointmentList = new ArrayList<>(); //holds all the upcoming appointments
    //fills with only the specific doctor's upcoming appointments using firebase query
    public static List<Appointment> approvedAppointmentList = new ArrayList<>();
    public static List<Appointment> pastAppointmentList = new ArrayList<>();


    Query appointmentsQuery;
    Button acceptAll;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pending_appointment_page);
        acceptAll = findViewById(R.id.accept_all);
        RecyclerView recyclerViewPending = findViewById(R.id.appointment_recyclerView);
        SwitchMaterial autoApprove = findViewById(R.id.autoApprove);



        Context context = this;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //currently signed in user
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference userRef = usersRef.child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Boolean autoApproveAppointments = snapshot.child("autoApproveAppointments").getValue(Boolean.class);
                Log.d("info","is auto approve enabled: "+ autoApproveAppointments);
                autoApprove.setChecked(autoApproveAppointments);

                autoApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d("info","auto-accept checked to: "+ autoApprove.isChecked());
                        snapshot.getRef().child("autoApproveAppointments").setValue(autoApprove.isChecked());
                    }
                });

                if(autoApproveAppointments){
                    approveAll();
                    recyclerViewPending.setLayoutManager(new LinearLayoutManager(context));
                    recyclerViewPending.setAdapter(new PendingAppointmentAdapter(getApplicationContext()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //strictly to read the name of the current doctor
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //query to get the appointments associated with the right doctor
                String employeeNumber = snapshot.child("employeeNumber").getValue(String.class);
                Log.d("Info","DOCTOR NAME: "+employeeNumber);
                appointmentsQuery = appointmentsRef.orderByChild("doctorID").equalTo(employeeNumber);


                appointmentsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        upcomingAppointmentList.clear();
                        approvedAppointmentList.clear();
                        pastAppointmentList.clear();
                        // Iterate through the dataSnapshot to get the appointments
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if(appointment != null){
                                if(!appointment.getIsPastAppointment()){
                                    if(appointment.getStatus().equals("Pending")){
                                        upcomingAppointmentList.add(appointment);

                                    } else if(appointment.getStatus().equals("Approved")){
                                        approvedAppointmentList.add(appointment);
                                    }
                                } else{
                                    Log.d("info","past appointments only :/");
                                }




                            }

                        }
                        Log.d("INFO", "pending size: "+upcomingAppointmentList.size());
                        Log.d("INFO","approved size: "+approvedAppointmentList.size());
                        //send data to the adapter to bind it to the view
                        recyclerViewPending.setLayoutManager(new LinearLayoutManager(context));
                        recyclerViewPending.setAdapter(new PendingAppointmentAdapter(getApplicationContext()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        acceptAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFO","Approve all clicked.");
                approveAll();
                //upcomingAppointmentList.clear();
                Log.d("info","Approvedlist.size: "+approvedAppointmentList.size());
                Log.d("info","pendinglist.size: "+upcomingAppointmentList.size());
                recyclerViewPending.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewPending.setAdapter(new PendingAppointmentAdapter(getApplicationContext()));

            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(listener ->{
            Log.d("info","item id clicked: "+listener.getItemId());
            if(listener.getItemId() == R.id.menu_approved){
                //swtiched to approved appointments page
                Intent intent = new Intent(UpcomingAppointments.this, ApprovedAppointments.class);
                startActivity(intent);
                return true;
            } else{
                return true;
            }
        });
    }

    public void approveAll(){
        for(Appointment appointment : upcomingAppointmentList){

            approvedAppointmentList.add(appointment);
            //find the specific appointment by its ID
            Query appointmentsQuery = appointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID());
            Log.d("INFO", "Appointment for id: "+appointment.getAppointmentID());
            appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Log.d("INFO","nothing in the query");
                    }
                    Log.d("INFO","in on data change method");
                    for(DataSnapshot appointmentSnapshot : snapshot.getChildren()){
                        //reference the specific appointment
                        DatabaseReference appointmentRef = appointmentSnapshot.getRef();
                        appointmentRef.child("status").setValue("Approved");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
