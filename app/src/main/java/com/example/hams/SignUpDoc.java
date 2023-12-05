package com.example.hams;
import android.annotation.SuppressLint;
import android.text.TextUtils;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;

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
    FirebaseDatabase database = MainActivity.database;
    DatabaseReference usersRef = MainActivity.usersRef;
    private Spinner specialtySpinner;
    private ArrayAdapter<String> specialtyAdapter;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_doc);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.specialtyAutoComplete);
        String[] specialties = {"Family Medicine", "Internal Medicine", "Pediatrics", "Obstetrics", "Gynecology"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, specialties);
        autoCompleteTextView.setAdapter(adapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //initialize editText elements
        button = (Button) findViewById(R.id.button);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        employee = findViewById(R.id.employee);
        add = findViewById(R.id.address);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //read input values
                String specialty = autoCompleteTextView.getText().toString();
                String emailAddress = String.valueOf(email.getText());
                String password = String.valueOf(pass.getText());
                String firstname = String.valueOf(first.getText());
                String lastname = String.valueOf(last.getText());
                String phoneNo = String.valueOf(phone.getText());
                String employeeNo = String.valueOf(employee.getText());
                String address = String.valueOf(add.getText());

                //submit registration info
                clearErrors();

                // Perform validations
                if (firstname.isEmpty() || lastname.isEmpty() || phoneNo.isEmpty() || employeeNo.isEmpty() || address.isEmpty() || emailAddress.isEmpty() || TextUtils.isEmpty(specialty) ) {
                    // Show error message if any field is empty
                    autoCompleteTextView.setError("Please select a specialty");
                    showInputError("All fields are required");



                }else if (!isValidName(firstname)) {
                    // Show error message if first name contains non-alphabetic characters
                    showInputError("Invalid first name");
                } else if (!isValidName(lastname)) {
                    // Show error message if last name contains non-alphabetic characters
                    showInputError("Invalid last name");
                } else if (phoneNo.length() != 10 || !TextUtils.isDigitsOnly(phoneNo)) {
                    // Show error message if phone number is not 10 digits or contains non-digit characters
                    showInputError("Invalid phone number (should be 10 digits)");
                } else if (employeeNo.length() != 10 || !TextUtils.isDigitsOnly(employeeNo)) {
                    // Show error message if employee number is not 10 digits or contains non-digit characters
                    showInputError("Invalid employee number (should be 10 digits)");
                } else if (!isValidEmail(emailAddress)) {
                    // Show error message if email address is invalid
                    showInputError("Invalid email address");
                } else {

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
                                doctor.setSpecialties(specialty);
                                doctor.setUserName(emailAddress);
                                doctor.setPassWord(password);
                                doctor.setStatus("Pending");
                                doctor.setType("Doctor");

                                fUser = mAuth.getCurrentUser();
                                String IDstring = fUser.getUid();

                                usersRef.child(IDstring).setValue(doctor);

                                Toast.makeText(SignUpDoc.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpDoc.this, "Authentication failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                    openLogin();
                }
            }
            private void showInputError(String errorMessage) {
                // Display error message within the appropriate input field only if it's empty
                if (first.getText().toString().trim().isEmpty()|| !isValidName(first.getText().toString().trim())) {
                    first.setError(errorMessage);
                }
                if (last.getText().toString().trim().isEmpty()|| !isValidName(last.getText().toString().trim())) {
                    last.setError(errorMessage);
                }
                if (phone.getText().toString().trim().isEmpty()|| phone.getText().toString().length() != 10 || !TextUtils.isDigitsOnly(phone.getText().toString())) {
                    phone.setError(errorMessage);
                }
                if (employee.getText().toString().trim().isEmpty() ) {
                    employee.setError(errorMessage);
                }
                if (add.getText().toString().trim().isEmpty()) {
                    add.setError(errorMessage);
                }
                if (email.getText().toString().trim().isEmpty()|| !isValidEmail(email.getText().toString().trim())) {
                    email.setError(errorMessage);
                }

            }

            private void clearErrors() {
                // Clear error messages from all input fields
                first.setError(null);
                last.setError(null);
                phone.setError(null);
                employee.setError(null);
                add.setError(null);
                email.setError(null);

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
                //  only alphabetic characters
                return name.matches("[a-zA-Z]+");
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginDoc.class);
        startActivity(intent);
    }

}