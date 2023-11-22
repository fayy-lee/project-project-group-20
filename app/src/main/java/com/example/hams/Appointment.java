package com.example.hams;
import java.util.Calendar;
import java.util.Date;
import android.os.Build;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Appointment {

    private String date;
    //private String dateString;

    private String startTime;
    private String endTime;
    private Patient patient;
    private String patientName;

    private String doctorID;
    private String status;
    private String appointmentID;
    private boolean isPastAppointment;

    public Appointment(){

        this.setStatus("Pending");
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setAppointmentID(String id){
        this.appointmentID = id;
    }
    public String getAppointmentID(){
        return appointmentID;
    }

    public void setDate(String dateString){
        /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the string into a LocalDate using the defined formatter
        LocalDate localDate = LocalDate.parse(dateString, formatter);*/

        this.date = dateString;
    }

    public String getDate(){
       return this.date;
    }


    public Patient getPatient() {
        return patient;
    }
    public String getPatientName(){
        return patient.getFirstName();
    }
    public void setPatientName(String name){
        patientName = name;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
    public void setIsPastAppointment(){
        return;
    }
    public boolean getIsPastAppointment() {
        if (date == null || date.isEmpty()) {
            return false; // Handle the case where 'date' is empty or null
        }

        // Assuming 'date' is in the format "YYYY-MM-DD"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar appointmentDate = Calendar.getInstance();

        try {
            // Parse the 'date' string into a Date object
            Date parsedDate = dateFormat.parse(date);

            // Set the Calendar instance to the parsed date
            appointmentDate.setTime(parsedDate);

            // Get the current date using Calendar
            Calendar currentDate = Calendar.getInstance();

            // Check if the appointment date is before the current date
            return appointmentDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parse exception if the date format is incorrect
            return false;
        }
    }

}