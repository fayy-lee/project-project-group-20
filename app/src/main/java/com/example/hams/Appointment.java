package com.example.hams;

public class Appointment {

    private String date;

    private String startTime;
    private Patient patient;
    private String patientName;

    private String doctorID;
    private String status;
    private String appointmentID;

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

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
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
    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

}
