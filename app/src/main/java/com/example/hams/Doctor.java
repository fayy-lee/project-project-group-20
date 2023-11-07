package com.example.hams;

import java.util.ArrayList;

public class Doctor extends User{
    String firstName;
    String lastName;
    String phoneNumber;
    String address;
    String employeeNumber;
    String specialties;

    public Doctor(){
        super();
        upcomingAppointments = new ArrayList<>();
        pastAppointments= new ArrayList<>();
        shifts = new ArrayList<>();
        autoApproveAppointments = false;

    }
    /*public Doctor(String user, String pass, String first, String last,
                String phone, String address,
                String number, String specialties) {
        super(user, pass);
        // fully initialize the doctor's attributes
            this.firstName = first;
            this.lastName = last;
            this.phoneNumber = phone;
            this.address = address;
            this.employeeNumber = number;
            this.specialties = specialties;

            //later send a message to the admin to approve the registration and activate the account
            setActive(true);
            //automatically set to active, WILL CHANGE LATER
        
    }*/

    public String getAddress() {
        return address;
    }
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getSpecialties() {
        return specialties;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    // making an list of pst and upcoming appointments.

    public List <Appointment> getUpcomingAppointments(){
        return upcomingAppointments;
    };
    public List <Appointment> getPastAppointments(){
        return pastAppointments;
    };
    public List <Shift> getShifts(){
        return shifts;
    }
    public boolean isAutoApproveAppointment(){
        return autoApproveAppointments;
    }
    public void addUpcomingAppointment(Appointment appointment){
        upcomingAppointments.add(appointment);
    }
    public void addPastAppoitment (Appointment appointment){
    }


}
