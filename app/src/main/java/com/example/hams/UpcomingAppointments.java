package com.example.hams;



import static com.example.hams.MainActivity.shiftRef;

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
    DatabaseReference shiftsRef = MainActivity.shiftRef;
    public static List<Appointment> upcomingAppointmentList = new ArrayList<>(); //holds all the upcoming appointments
    //fills with only the specific doctor's upcoming appointments using firebase query
    public static List<Appointment> approvedAppointmentList = new ArrayList<>();
    public static List<Appointment> pastAppointmentList = new ArrayList<>();


    Query appointmentsQuery;
    Query shiftsQuery;
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
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingAppointmentList.clear();
                //query to get the appointments associated with the right doctor
                String employeeNumber = snapshot.child("employeeNumber").getValue(String.class);
                Log.d("Info","DOCTOR NAME: "+employeeNumber);
                //appointmentsQuery = appointmentsRef.orderByChild("doctorID").equalTo(employeeNumber);
                shiftsQuery = shiftsRef.orderByChild("doctorID").equalTo(employeeNumber);
                shiftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Log.d("info","shift query empty");
                        }
                        for (DataSnapshot shiftSnapshot : snapshot.getChildren()) {
                            Shift shift = shiftSnapshot.getValue(Shift.class);
                            for(Appointment a : shift.getShiftAppointments()){
                                Log.d("info","shift appointment status: "+a.getStatus());
                                if(a.getStatus().equals("Pending")){
                                    Log.d("info","adding to list");
                                    upcomingAppointmentList.add(a);
                                }
                            }
                        }
                        Log.d("info", "upcoming appointments size: " + upcomingAppointmentList.size());
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
            appointment.setStatus("Approved");
            Query shiftsQuery = shiftsRef.orderByChild("doctorID").equalTo(appointment.getDoctorID());
            shiftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot shiftSnapshot : snapshot.getChildren()){
                        //for each shift, now we search its shiftAppointments for the one with appointmentID
                        String shiftId = shiftSnapshot.getKey();
                        DatabaseReference shiftAppointmentsRef = shiftsRef.child(shiftId).child("shiftAppointments");
                        shiftAppointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //now change the status of the appointment
                                for(DataSnapshot shiftAppointmentSnapshot : snapshot.getChildren()){
                                    //reference the specific appointment
                                    DatabaseReference appointmentRef = shiftAppointmentSnapshot.getRef();

                                    //change status, remove from bookable options
                                    appointmentRef.child("status").setValue("Approved");

                                    upcomingAppointmentList.remove(appointment);
                                    approvedAppointmentList.add(appointment);
                                }
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
    }
}
