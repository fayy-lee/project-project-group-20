package com.example.hams;

public class Doctor extends User{
    String firstName;
    String lastName;
    String phoneNumber;
    String address;
    String employeeNumber;
    String[] specialties;


    public Doctor(String user, String pass, String first, String last,
                String phone, String address,
                String number, String[] specialties) {
        super(user, pass);
        // fully initialize the doctor's attributes
            this.firstName = first;
            this.lastName = last;
            this.phoneNumber = phone;
            this.address = address;
            this.employeeNumber = number;
            this.specialties = specialties;

            //later send a message to the admin to approve the registration and activate the account
            active = true;
            //automatically set to active, WILL CHANGE LATER
        
    }

                    
                        
    
}
