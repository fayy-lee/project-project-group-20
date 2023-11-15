package com.example.hams;


public class Appointment {

    private String date;

    private String startTime;
    private String endTime;
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
    // Calculate end time assuming a 1-hour duration
    private String calculateEndTime(String startTime) {
        // Assuming startTime is in "HH:mm" format and appointments last 1 hour
        int hour = Integer.parseInt(startTime.split(":")[0]);
        int minute = Integer.parseInt(startTime.split(":")[1]);
        hour += 1; // Add one hour for end time

        // Check if adding an hour goes into the next day
        if (hour == 24) {
            hour = 0; // Reset hour to '00' if it's 24
        }

        // Return end time in "HH:mm" format
        return String.format("%02d:%02d", hour, minute);
    }

}