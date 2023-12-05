package com.example.hams;

import android.content.Context;  // Import the Context class
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminRejected extends AppCompatActivity {

    List<User> rejectedUsers = AdminPending.rejectedUsers;
    DatabaseReference usersRef = MainActivity.usersRef;
    Query rejectedUsersQuery = usersRef.orderByChild("status").equalTo("Rejected");
    public void setApprovedView(){
        Intent intent = new Intent(this, AdminApproved.class);
        startActivity(intent);
    }
    public void setPendingView(){
        Intent intent = new Intent(this, AdminPending.class);
        startActivity(intent);
    }
    public void setRejectedView(){
        Intent intent = new Intent(this, AdminRejected.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rejected);
        Log.d("Info", "ON REJECTED PAGE");

        RecyclerView recyclerViewRejected = findViewById(R.id.recyclerview);
        //RecyclerView recyclerViewApproved = findViewById(R.id.recyclerviewApproved);

        Context context = this;


        //show pending patients
        rejectedUsersQuery.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rejectedUsers.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Info", "checking elements in Rejected");
                    String userType = userSnapshot.child("type").getValue(String.class);


                    if(userType.equals("Patient")){
                        Log.d("Info", "patient detected in firebase");
                        //only add patients to this list, because only patients will fit with this type of view
                        Patient patient = userSnapshot.getValue(Patient.class);
                        if (patient != null) {
                            //add to the list
                            rejectedUsers.add(patient);
                            Log.d("Info","p: "+patient.getFirstName());
                        }

                    } else if (userType.equals("Doctor")) {
                        Log.d("Info", "doctor detected in firebase");
                        //only add patients to this list, because only patients will fit with this type of view
                        Doctor doctor = userSnapshot.getValue(Doctor.class);
                        if (doctor != null) {
                            //add to the list
                            rejectedUsers.add(doctor);
                            Log.d("Info", "just added d: " + doctor.getFirstName());
                        }
                    }
                }
                //send data to the adapter to bind it to the view
                recyclerViewRejected.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewRejected.setAdapter(new RejectedAdapter(getApplicationContext()));
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView. setOnItemSelectedListener(listener -> {



            Log.d("Info", "item id/page: " + listener.getItemId());
            if(listener.getItemId() == R.id.menu_approved){
                Log.d("Info", "SHOULD BE SETTING APPROVED");
                setApprovedView();
                return true;
            } else if(listener.getItemId() == R.id.menu_pending){
                Log.d("Info", "SHOULD BE SETTING PENDING");
                setPendingView();
                return true;
            } else if(listener.getItemId() == R.id.menu_rejected){
                Log.d("Info", "SHOULD BE SETTING REJECTED");
                setRejectedView();
                return true;
            } else{
                Log.d("info", "no buttons pressed");
                return false;
            }

        });

    }
}
