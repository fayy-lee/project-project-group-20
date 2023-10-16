package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Patient extends User{

    String firstName;
    String lastName;
    String phoneNo;
    String address;
    String healthCard;
    String emailAddress;

    public Patient(String firstName, String lastName, String email, String pass, String user, String phoneNo, String address, String healthCard){
        
        //calling superclass User's constructor
        super(user, pass); 

        //initializing Patient class' objects
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.address = address;
        this.healthCard = healthCard;
        this.emailAddress = email;

        //will be used in the future when seeking approval from Admin
        setActive(true);
    }
}
