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

    /*public Patient(String firstName, String lastName, String email, String pass, String user, String phoneNo, String address, String healthCard){
        
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
    } */

    public Patient(){ //empty constructor, necessary for firebase stuff
        super();
    }

    //setters and getter
    public String getAddress() {
        return address;
    }
    public String getHealthCard(){
        return healthCard;
    }
    public String getPhoneNumber() {
        return phoneNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getPhoneNo(){
        return phoneNo;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNo = phoneNumber;
    }

    public void setHealthCard(String healthCard){
        this.healthCard = healthCard;
    }
    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }
    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
}
