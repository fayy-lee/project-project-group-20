package com.example.hams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class AdminPending extends AppCompatActivity {
    private TextView textView;
    private ImageButton reject, accept;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Requests").child("Pending");

    Query pendingUsersQuery = usersRef.orderByChild("status").equalTo("Pending");

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
        setContentView(R.layout.activity_admin_pending);

        Log.d("Info", "ON PENDIng PAGE");

        RecyclerView recyclerViewPending = findViewById(R.id.recyclerviewPending);

        List<Patient> pendingPatients = new ArrayList<>(); //instantiate list of patients
        Context context = this;


        //show pending patients
        pendingUsersQuery.addValueEventListener(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingPatients.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Info", "checking elements in Pending");
                    String userType = userSnapshot.child("type").getValue(String.class);


                    if (userType.equals("Patient")) {
                        Log.d("Info", "patient detected in firebase");
                        //only add patients to this list, because only patients will fit with this type of view
                        Patient patient = userSnapshot.getValue(Patient.class);
                        if (patient != null) {
                            //add to the list
                            pendingPatients.add(patient);
                            Log.d("Info", "p: " + patient.getFirstName());
                        }
                    }
                }
                //send data to the adapter to bind it to the view
                recyclerViewPending.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewPending.setAdapter(new MyAdapter(getApplicationContext(), pendingPatients));
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Log.d("Info", "item id/page: " + item.getItemId());
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
