package com.example.hams;

import android.annotation.SuppressLint;
import android.content.Context;  // Import the Context class
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
import java.util.concurrent.atomic.AtomicReference;

public class AdminApproved extends AppCompatActivity {

    private TextView receivedText;
    private SharedPreferences sharedPreferences;
    private TextView textView;
    private ImageButton reject, accept;
    private BottomNavigationView bottomNavigationView;
    DatabaseReference usersRef = MainActivity.usersRef;
    Query approvedUsersQuery = usersRef.orderByChild("status").equalTo("Approved");
    List<User> approvedUsers = AdminPending.approvedUsers;

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
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approved);

        Log.d("Info", "ON APPROVED PAGE");

        RecyclerView recyclerViewApproved = findViewById(R.id.recyclerview);

        Context context = this;


        //show elements
        approvedUsersQuery.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                approvedUsers.clear(); // Clear the list to avoid duplicates
                Log.d("Info", "in Approved");
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Info", "checking elements in Approved");
                    String userType = userSnapshot.child("type").getValue(String.class);


                    if(userType.equals("Patient")){
                        Log.d("Info", "patient detected in firebase (A)");
                        //only add patients to this list, because only patients will fit with this type of view
                        Patient patient = userSnapshot.getValue(Patient.class);
                        if (patient != null) {
                            //add to the list
                            approvedUsers.add(patient);
                            Log.d("Info","p: "+patient.getFirstName());
                        }
                    } else if (userType.equals("Doctor")) {
                        Log.d("Info", "doctor detected in firebase");
                        //only add patients to this list, because only patients will fit with this type of view
                        Doctor doctor = userSnapshot.getValue(Doctor.class);
                        if (doctor != null) {
                            //add to the list
                            approvedUsers.add(doctor);
                            Log.d("Info", "just added d: " + doctor.getFirstName());
                        }
                    }
                }

                recyclerViewApproved.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewApproved.setAdapter(new ApprovedAdapter(getApplicationContext()));
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case 2131362091: //int value of the R.id.menu_approved
                    // Load "Approved" users
                    Log.d("Info", "SHOULD BE SETTING APPROVED");
                    setApprovedView();
                    return true;
                case 2131362092: //pending view
                    Log.d("Info", "SHOULD BE SETTING PENDING");
                    setPendingView();
                    return true;
                case 2131362093: //R.id.menu_rejected
                    //IMPLEMENT THIS WITH REJCTED AS WELL
                    Log.d("Info", "SHOULD BE SETTING REJECTED");
                    setRejectedView();
                    return true;
                default:
                    Log.d("Info", "DEFAULTING");
                    return false;
            }

        });

    }

}

