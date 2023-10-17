package com.example.hams;


import android.content.Intent;
import android.os.Bundle;
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

public class SignUpPat extends AppCompatActivity{
    private Button button;
    TextInputEditText user;
    TextInputEditText pass;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText phone;
    TextInputEditText add;
    TextInputEditText email;
    TextInputEditText HCN;

    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference rootRef, newRef;

    public Patient patient;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_pat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();

        button = (Button) findViewById(R.id.button);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        add = findViewById(R.id.address);
        email = findViewById(R.id.email);
        HCN = findViewById(R.id.healthCard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //read input values
                String username = String.valueOf(user.getText());
                String password = String.valueOf(pass.getText());
                String firstname = String.valueOf(first.getText());
                String lastname = String.valueOf(last.getText());
                String phoneNo = String.valueOf(phone.getText());
                String address = String.valueOf(add.getText());
                String emailAddress = String.valueOf(email.getText());
                String healthCard = String.valueOf(HCN.getText());

                //submit registration info and create patient object
                patient = new Patient(firstname,lastname,emailAddress,password,username,phoneNo,address,healthCard);

                mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            fUser = mAuth.getCurrentUser();
                            String IDstring = fUser.getUid();


                            rootRef.child("users").child(IDstring).setValue(patient);

                            Toast.makeText(SignUpPat.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpPat.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                openLogin();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginPat.class);
        startActivity(intent);
    }
}