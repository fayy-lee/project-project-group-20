package com.example.hams;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class Appointment {

    private String date;
    private String startTime;
    private String endTime;
    private String associatedDoctorId;
    private String associatedPatientId;
    private String status;
    public  Appointment(){}
    // Constructor
    public Appointment(String date, String startTime, String associatedDoctorId, String associatedPatientId) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setAssociatedDoctorId(associatedDoctorId);
        appointment.setAssociatedPatientId(associatedPatientId);
        appointment.setEndTime(calculateEndTime(startTime));
        appointment.setStatus("Pending");


        // Add to Firebase database
        saveAppointmentToDatabase();
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

    // Save the appointment to the database
    private void saveAppointmentToDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference appointmentsRef = databaseReference.child("appointments");

        // Create a map of appointment details
        Map<String, Object> appointmentDetails = new HashMap<>();
        appointmentDetails.put("date", this.date);
        appointmentDetails.put("startTime", this.startTime);
        appointmentDetails.put("endTime", this.endTime);
        appointmentDetails.put("associatedDoctorId", this.associatedDoctorId);
        appointmentDetails.put("associatedPatientId", this.associatedPatientId);
        appointmentDetails.put("status", this.status);

        // Push the appointment to Firebase, generating a unique key
        appointmentsRef.push().setValue(appointmentDetails);
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAssociatedDoctorId() {
        return associatedDoctorId;
    }

    public void setAssociatedDoctorId(String associatedDoctorId) {
        this.associatedDoctorId = associatedDoctorId;
    }

    public String getAssociatedPatientId() {
        return associatedPatientId;
    }

    public void setAssociatedPatientId(String associatedPatientId) {
        this.associatedPatientId = associatedPatientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}