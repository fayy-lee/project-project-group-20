package com.example.hams;
public class Patient extends User{


    public Patient(string firstName, string lastName, string emailAddress, string password, string username, string phoneNo, string address, string healthCard){
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNo = phoneNo;
        this.address = address;
        this.healthCard = healthCard;
        boolean active = true;
    }
    
    
}
