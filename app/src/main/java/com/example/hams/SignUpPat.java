package com.example.hams;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpPat extends AppCompatActivity{
    private Button button;
    public static Patient pat;
    TextInputEditText user;
    TextInputEditText pass;
    TextInputEditText first;
    TextInputEditText last;
    TextInputEditText phone;
    TextInputEditText add;
    TextInputEditText email;
    TextInputEditText HCN;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_pat);

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
                pat = new Patient(firstname,lastname,emailAddress,password,username,phoneNo,address,healthCard);
                openLogin();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginPat.class);
        startActivity(intent);
    }
}