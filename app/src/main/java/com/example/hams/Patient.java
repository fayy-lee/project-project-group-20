package com.example.hams;
public class Patient extends User{

    string firstName;
    string lastName;
    string emailAddress;
    string phoneNo;
    string address;
    string healthCard;

    public Patient(string firstName, string lastName, string emailAddress, string pass, string user, string phoneNo, string address, string healthCard){
        
        //calling superclass User's constructor
        super(user, pass); 

        //initializing Patient class' objects
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNo = phoneNo;
        this.address = address;
        this.healthCard = healthCard;

        //will be used in the future when seeking approval from Admin
        boolean active = true;
    }
    

}
