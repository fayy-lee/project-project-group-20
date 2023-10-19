package com.example.hams;



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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpDoc extends AppCompatActivity {

    private Button button;
    TextInputEditText email;
    TextInputEditText pass;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText phone;
    TextInputEditText employee;
    TextInputEditText add;
    TextInputEditText special;
    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference rootRef, newRef;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_doc);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();

        //initialize editText elements
        button = (Button) findViewById(R.id.button);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        employee = findViewById(R.id.employee);
        add = findViewById(R.id.address);
        special = findViewById(R.id.special);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read input values
                String emailAddress = String.valueOf(email.getText());
                String password = String.valueOf(pass.getText());
                String firstname = String.valueOf(first.getText());
                String lastname = String.valueOf(last.getText());
                String phoneNo = String.valueOf(phone.getText());
                String employeeNo = String.valueOf(employee.getText());
                String address = String.valueOf(add.getText());
                String specialties = String.valueOf(special.getText());
                //submit registration info


                mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            Doctor doctor = new Doctor();
                            doctor.setFirstName(firstname);
                            doctor.setLastName(lastname);
                            doctor.setAddress(address);
                            doctor.setPhoneNumber(phoneNo);
                            doctor.setEmployeeNumber(employeeNo);
                            doctor.setSpecialties(specialties);
                            doctor.setUserName(emailAddress);
                            doctor.setPassWord(password);

                            fUser = mAuth.getCurrentUser();
                            String IDstring = fUser.getUid();

                            rootRef.child("Users").child("Doctors").child(IDstring).setValue(doctor);

                            Toast.makeText(SignUpDoc.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpDoc.this, "Authentication failed", Toast.LENGTH_LONG).show();

                        }
                    }
                });
                openLogin();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginDoc.class);
        startActivity(intent);
    }

}