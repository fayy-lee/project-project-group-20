package com.example.hams;

import static com.example.hams.UpcomingAppointments.upcomingAppointmentList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginPat extends AppCompatActivity {

    private Button button;
    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    private FirebaseDatabase database = MainActivity.database;
    DatabaseReference usersRef = MainActivity.usersRef;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_pat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        button = (Button) findViewById(R.id.button);
        TextInputEditText userBox = findViewById(R.id.user);
        TextInputEditText passwordBox = findViewById(R.id.pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = String.valueOf(userBox.getText());
                String password = String.valueOf(passwordBox.getText());

                mAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user == null){
                                Log.d("Info","in checkStatus");
                            } else{
                                checkStatus(user);
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Info", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPat.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
    public void openLogin(String patientID){

        //TODO: when patient functionality is here, make them create the appointments, not in the log in here
        //TODO: when a patient signs in, get their health card number
        //this was only for testing purposes so we'd have a set of
        for(int i = 1; i<6; i++){
            Appointment testA = new Appointment();
            testA.setDate("2023-12-01");
            testA.setStartTime("2:00 pm");
            Patient p = new Patient();
            p.setFirstName("Patient " + i);
            p.setHealthCard("1234567890");
            testA.setPatient(p);
            testA.setDoctorID("0000000000");
            testA.setPatientID(patientID);
            testA.setStatus("Approved");
            String appointmentId = appointmentsRef.push().getKey();
            testA.setAppointmentID(appointmentId);

            if(testA.getIsPastAppointment()){
                Log.d(" INFO","PAST APPOINTMENT CREATED");
            }
            appointmentsRef.child(appointmentId).setValue(testA);

            upcomingAppointmentList.add(testA);
            Log.d("Info", "appointment added: " + i);
        }
        Intent intent = new Intent(this, PatView.class);
        startActivity(intent);
    }
    public void openSplash(){
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
    /*
    Method to check the status of the user based on the firebase attribute "status"
    If approved, login is successful, if not, a toast is shown and the user is sent to the splash page
     */
    private void checkStatus(FirebaseUser user){
        //reference the database elements connected to the user
        DatabaseReference userRef = usersRef.child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //now check what we wanna do
                    String userStatus = snapshot.child("status").getValue(String.class);
                    if(userStatus.equals("Approved")){
                        //proceed to login as normal
                        Toast.makeText(LoginPat.this, "Login successful.",
                                Toast.LENGTH_SHORT).show();
                        String patientID = snapshot.child("healthCard").getValue(String.class);
                        openLogin(patientID);
                    } else if(userStatus.equals("Pending")){
                        //give pending message, send back to splash
                        Toast.makeText(LoginPat.this, "Account approval pending.",
                                Toast.LENGTH_SHORT).show();
                        openSplash();
                    } else if(userStatus.equals("Rejected")){
                        //give rejected mesage, send to splash
                        Toast.makeText(LoginPat.this, "Account approval rejected.",
                                Toast.LENGTH_SHORT).show();
                        openSplash();
                    }
                }else{
                    //there is no data found for that user in that directory
                    Log.d("Info","no data snapshot for patient");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}