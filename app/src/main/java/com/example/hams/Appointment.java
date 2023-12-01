package com.example.hams;
import java.util.Calendar;
import java.util.Date;
import android.os.Build;
import android.util.Log;

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
    private String patientID;
    private Doctor doctor;
    private String doctorID;
    private String status;
    private String appointmentID;
    private boolean isPastAppointment;
    private boolean withinAnHour;
    private String specialty;

    public Appointment(){

        this.setStatus("Not Booked");
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
    public Doctor getDoctor(){
        return this.doctor;
    }
    public String getSpecialty(){
        return this.doctor.getSpecialties();
    }



    public Patient getPatient() {
        return patient;
    }
    public String getPatientID(){
        return patientID;
    }
    public void setPatientID(String id){
        patientID = id;
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
    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
        this.doctorID = doctor.getEmployeeNumber();
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

        //if date isn't past, still check time
        LocalTime startTime = LocalTime.parse(this.startTime);
        LocalDate date = LocalDate.parse(this.date);
        LocalDateTime appointmentDateTime = LocalDateTime.of(date, startTime);

        if(appointmentDateTime.isBefore(LocalDateTime.now())){
            Log.d("info","appointment is in the past according to localDateTime");
            return true;
        }else{
            return false;
        }
        /*
        if(date.isBefore(LocalDate.now())){
            Log.d("info","appointment is in the past according to localDate");
            return true;
        } else if(startTime.isBefore(LocalTime.now())){
            Log.d("info","appointment is in the past according to localTime");
            Log.d("info","appt date: "+ startTime + " VS current date: "+LocalTime.now());
            return true;
        }
        withinAnHour = false; //past appointments aren't within an hour
        return false;*/



    }
    public boolean isWithinAnHour(){
        withinAnHour = false;
        //get the start date and time of appointment as local date/time objects
        LocalDate date = LocalDate.parse(this.date);
        LocalTime startTime = LocalTime.parse(this.startTime);
        //check if the date is today
        if(date.isEqual(LocalDate.now())){
            //check if the time is within an hour
            if(LocalTime.now().isAfter(startTime.minusHours(1))){
                withinAnHour = true;
            }
        }
        return withinAnHour;
    }
    public void setWithinAnHour(Boolean w){
        //android studio/firebase complained about this so here we are
        this.withinAnHour = w;
    }

}