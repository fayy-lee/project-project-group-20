package com.example.hams;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;

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

public class PastAppointments extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference usersRef = MainActivity.usersRef;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;

    List<Appointment> pastAppointmentList = UpcomingAppointments.pastAppointmentList;
    Query appointmentsQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.past_appointment_page);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        Context context = this;

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
                appointmentsQuery = appointmentsRef.orderByChild("doctorID").equalTo(employeeNumber);
                //now read from the query data
                appointmentsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pastAppointmentList.clear();
                        // Iterate through the dataSnapshot to get the appointments
                        for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                            Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                            if(appointment != null){
                                Log.d("info","ispast? "+ appointment.getIsPastAppointment());
                                if(appointment.getIsPastAppointment()){
                                    pastAppointmentList.add(appointment);
                                }
                                Log.d(" info"," past appointment size: "+pastAppointmentList.size());
                            }

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(new PastAppointmentAdapter(getApplicationContext()));
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
