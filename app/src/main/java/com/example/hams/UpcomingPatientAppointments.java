package com.example.hams;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UpcomingPatientAppointments extends AppCompatActivity {
    DatabaseReference usersRef = MainActivity.usersRef;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    List<Appointment> upcomingAppointmentList = UpcomingAppointments.upcomingAppointmentList;
    List<Appointment> approvedAppointmentList = UpcomingAppointments.approvedAppointmentList;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userRef;
    Query appointmentsQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.patient_appointment_page);
        Context context = this;
        RecyclerView recyclerViewPending = findViewById(R.id.recyclerView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userRef = usersRef.child(user.getUid());
        Log.d("info","approved appointment size: "+approvedAppointmentList.size());


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //query to get the appointments associated with the right patient
                String healthCard = snapshot.child("healthCard").getValue(String.class);

                appointmentsQuery = appointmentsRef.orderByChild("patientID").equalTo(healthCard);

                appointmentsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        approvedAppointmentList.clear();
                        // Iterate through the dataSnapshot to get the appointments
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if(appointment != null){
                                if(!appointment.getIsPastAppointment()){
                                    if(appointment.getStatus().equals("Approved")){
                                        if(appointment.getPatientID().equals(healthCard));
                                        approvedAppointmentList.add(appointment);
                                    }
                                }
                            }

                        }
                        Log.d("INFO", "approved pat appointment size: "+approvedAppointmentList.size());
                        //send data to the adapter to bind it to the view
                        recyclerViewPending.setLayoutManager(new LinearLayoutManager(context));
                        recyclerViewPending.setAdapter(new ApprovedAppointmentAdapter(getApplicationContext()));

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

    }
}
