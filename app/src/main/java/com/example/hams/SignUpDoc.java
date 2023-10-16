package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpDoc extends AppCompatActivity {

    private Button button;
    public static Doctor doc;
    TextInputEditText user;
    TextInputEditText pass;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText phone;
    TextInputEditText employee;
    TextInputEditText add;
    TextInputEditText special;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_doc);

        button = (Button) findViewById(R.id.button);
        user = findViewById(R.id.email);
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
                String username = String.valueOf(user.getText());
                String password = String.valueOf(pass.getText());
                String firstname = String.valueOf(first.getText());
                String lastname = String.valueOf(last.getText());
                String phoneNo = String.valueOf(phone.getText());
                String employeeNo = String.valueOf(employee.getText());
                String address = String.valueOf(add.getText());
                String specialties = String.valueOf(special.getText());
                //submit registration info
                doc = new Doctor(username,password,firstname,lastname,phoneNo,address,employeeNo,specialties);
                openLogin();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginDoc.class);
        startActivity(intent);
    }
}