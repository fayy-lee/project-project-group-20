package com.example.hams;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor extends User implements Serializable {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String employeeNumber;
    private String specialties;
    private ArrayList<Appointment> upcomingAppointments;
    private ArrayList<Appointment> pastAppointments;
    public ArrayList<Shift> shifts;
    private Boolean autoApproveAppointments;

    public Doctor(){
        super();
        shifts = new ArrayList<>();
        setAutoApproveAppointments(false);
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

    public Boolean getAutoApproveAppointments() {
        return autoApproveAppointments;
    }

    public void setAutoApproveAppointments(Boolean autoApproveAppointments) {
        this.autoApproveAppointments = autoApproveAppointments;
    }

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


    public ArrayList<Shift> getShifts(){
        return shifts;
    }

    public void addUpcomingAppointment(Appointment appointment){
        upcomingAppointments.add(appointment);
    }
    public void addPastAppoitment (Appointment appointment){
    }
    public ArrayList<Appointment> getUpcomingAppointments(){
        return upcomingAppointments;
    }


}
