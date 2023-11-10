package com.example.hams;

public class Appointment {

    String date;

    String startTime;
    Patient patient;

    Doctor doctor;

    public Appointment(){

    }
    public void setDate(String date){
        this.date = date;
    }


    public Patient getPatient() {
        return patient;
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
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

}
