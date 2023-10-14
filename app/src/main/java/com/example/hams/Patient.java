package com.example.hams;
public class Patient extends User{

    String firstName;
    String lastName;
    String emailAddress;
    String phoneNo;
    String address;
    String healthCard;

    public Patient(String firstName, String lastName, String emailAddress, String pass, String user, String phoneNo, String address, String healthCard){
        
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
        setActive(true);
    }
    

}
