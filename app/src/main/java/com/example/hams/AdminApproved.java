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
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Requests").child("Pending");
    Query approvedUsersQuery = usersRef.orderByChild("status").equalTo("Approved");

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

        List<Patient> approvedPatients = new ArrayList<>();
        Context context = this;


        //show elements
        approvedUsersQuery.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                approvedPatients.clear(); // Clear the list to avoid duplicates
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
                            approvedPatients.add(patient);
                            Log.d("Info","p: "+patient.getFirstName());
                        }
                    }
                }
                for(Patient p:approvedPatients){
                    Log.d("Info","APPROVED p: "+p.getFirstName());
                }

                recyclerViewApproved.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewApproved.setAdapter(new ApprovedAdapter(getApplicationContext(), approvedPatients));
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case 2131362341: //int value of the R.id.menu_approved
                    // Load "Approved" users
                    Log.d("Info", "SHOULD BE SETTING APPROVED");
                    setApprovedView();
                    return true;
                case 2131362342: //pending view
                    Log.d("Info", "SHOULD BE SETTING PENDING");
                    setPendingView();
                    return true;
                case 2131362343: //R.id.menu_rejected
                    //IMPLEMENT THIS WITH REJCTED AS WELL
                    Log.d("Info", "SHOULD BE SETTING REJECTED");
                    setRejectedView();
                    return true;
                default:
                    Log.d("Info", "DEFAULTINGGGG");
                    return false;
            }

        });

    }

}

