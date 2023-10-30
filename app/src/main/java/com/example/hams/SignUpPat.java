package com.example.hams;
import android.text.TextUtils;

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

public class SignUpPat extends AppCompatActivity{
    private Button button;
    TextInputEditText pass;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText phone;
    TextInputEditText add;
    TextInputEditText email;
    TextInputEditText HCN;

    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    FirebaseDatabase database = MainActivity.database;
    DatabaseReference usersRef = MainActivity.usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_pat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        button = (Button) findViewById(R.id.button);
        //user = findViewById(R.id.user);
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
                //String username = String.valueOf(user.getText());
                String password = String.valueOf(pass.getText());
                String firstname = String.valueOf(first.getText());
                String lastname = String.valueOf(last.getText());
                String phoneNo = String.valueOf(phone.getText());
                String address = String.valueOf(add.getText());
                String emailAddress = String.valueOf(email.getText());
                String healthCard = String.valueOf(HCN.getText());
                clearErrors();

                // Perform validations
                if (firstname.isEmpty() || lastname.isEmpty() || phoneNo.isEmpty() || address.isEmpty() || emailAddress.isEmpty() || healthCard.isEmpty()) {
                    // Show error message if any field is empty
                    showInputError("All fields are required");
                } else if (!isValidName(firstname)) {
                    // Show error message if first name contains non-alphabetic characters
                    showInputError("Invalid first name");
                } else if (!isValidName(lastname)) {
                    // Show error message if last name contains non-alphabetic characters
                    showInputError("Invalid last name");
                } else if (phoneNo.length() != 10 || !TextUtils.isDigitsOnly(phoneNo)) {
                    // Show error message if phone number is not 10 digits or contains non-digit characters
                    showInputError("Invalid phone number (should be 10 digits)");
                } else if (healthCard.length() != 10 || !TextUtils.isDigitsOnly(healthCard)) {
                    // Show error message if health card number is not 10 digits or contains non-digit characters
                    showInputError("Invalid health card number (should be 10 digits)");
                } else if (!isValidEmail(emailAddress)) {
                    // Show error message if email address is invalid
                    showInputError("Invalid email address");
                } else {

                    mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Patient patient = new Patient();
                                //attributes need to have a getter method to be added to the database
                                patient.setFirstName(firstname);
                                patient.setLastName(lastname);
                                patient.setAddress(address);
                                patient.setPhoneNumber(phoneNo);
                                patient.setUserName(emailAddress); //email
                                patient.setPassWord(password);
                                patient.setHealthCard(healthCard);
                                patient.setStatus("Pending");
                                patient.setType("Patient");


                                fUser = mAuth.getCurrentUser();
                                String IDstring = fUser.getUid();


                                //put the user in the database as pending
                                usersRef.child(IDstring).setValue(patient);

                                Toast.makeText(SignUpPat.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpPat.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    openLogin();
                }
            }
            private void showInputError(String errorMessage) {
                // Display error message within the appropriate input field
                if (first.getText().toString().trim().isEmpty() || !isValidName(first.getText().toString().trim())) {
                    first.setError(errorMessage);
                }
                if (last.getText().toString().trim().isEmpty() || !isValidName(last.getText().toString().trim())) {
                    last.setError(errorMessage);
                }
                if (phone.getText().toString().trim().isEmpty() || phone.getText().toString().length() != 10 || !TextUtils.isDigitsOnly(phone.getText().toString())) {
                    phone.setError(errorMessage);
                }
                if (add.getText().toString().trim().isEmpty()) {
                    add.setError(errorMessage);
                }
                if (email.getText().toString().trim().isEmpty() || !isValidEmail(email.getText().toString().trim())) {
                    email.setError(errorMessage);
                }
                if (HCN.getText().toString().trim().isEmpty() || HCN.getText().toString().length() != 10 || !TextUtils.isDigitsOnly(HCN.getText().toString())) {
                    HCN.setError(errorMessage);
                }
            }




            private void clearErrors() {
                // Clear error messages from all input fields
                first.setError(null);
                last.setError(null);
                phone.setError(null);
                add.setError(null);
                email.setError(null);
                HCN.setError(null);
            }
            private boolean isValidPhoneNumber(String phoneNumber) {

                return phoneNumber.length() == 10 && TextUtils.isDigitsOnly(phoneNumber);
            }

            private boolean isValidEmail(String email) {

                return !TextUtils.isEmpty(email);
            }

            private boolean isValidHealthCard(String healthCard) {

                return healthCard.length() == 10 && TextUtils.isDigitsOnly(healthCard);
            }
            private boolean isValidName(String name) {
                // Check if the name contains only alphabetic characters
                return name.matches("[a-zA-Z]+");
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginPat.class);
        startActivity(intent);
    }
}