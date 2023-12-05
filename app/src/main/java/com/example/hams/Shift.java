package com.example.hams;

import static com.example.hams.BookAppointments.bookableAppointmentList;
import static com.example.hams.MainActivity.appointmentsRef;
import static com.example.hams.MainActivity.usersRef;

import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalTime;
import org.threeten.bp.LocalDate;


public class Shift implements Serializable {
    private String date;
    private String startTime;
    private String endTime;
    private String doctorID;
    private transient Doctor doctor;
    private String shiftId;
    private boolean validTimeIncrement;
    public transient List<Appointment> shiftAppointments;

    public Shift(){

    }

    public Shift(String date, String startTime, String endTime, Doctor doctor) {
        new Shift();
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.shiftAppointments = new ArrayList<>();
        try{
            this.setDoctorID(doctor.getEmployeeNumber());
            this.setDoctor(doctor);
        }catch(NullPointerException e){
            Log.d("Info","Doctor is null??");
        }
        //generateShiftAppointments();
        //this.setDoctorID(doctor.getEmployeeNumber());
        //doctor.shifts.add(this);
    }
    public Shift(String date, String startTime, String endTime) {
        new Shift();
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }


    public void setShiftID(String id){this.shiftId = id;
    }
    public String getShiftID(){return shiftId ;}

    public String getdoctorID(){return doctorID;}
    public Doctor getDoctor(){return doctor;}
    public void setDoctor(Doctor doc){
        doctor = doc;
    }
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
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public boolean isValidTimeIncrement() {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        return start.getMinute() % 30 == 0 && end.getMinute() % 30 == 0;
    }

    public boolean isValidShift(LocalDate currentDate, List<Shift> existingShifts) {
        LocalDate shiftDate = LocalDate.parse(date);

        // Check if the date is in the future
        if (shiftDate.isBefore(currentDate)) {
            System.out.println("Error: Cannot add a shift for a past date.");
            return false;
        }

        // Check if the new shift conflicts with existing shifts
        for (Shift existingShift : existingShifts) {
            if (shiftDate.isEqual(LocalDate.parse(existingShift.getDate()))) {
                LocalTime newStartTime = LocalTime.parse(startTime);
                LocalTime newEndTime = LocalTime.parse(endTime);
                LocalTime existingStartTime = LocalTime.parse(existingShift.getStartTime());
                LocalTime existingEndTime = LocalTime.parse(existingShift.getEndTime());

                if ((newStartTime.isAfter(existingStartTime) && newStartTime.isBefore(existingEndTime)) ||
                        (newEndTime.isAfter(existingStartTime) && newEndTime.isBefore(existingEndTime))) {
                    System.out.println("Error: Shift conflicts with an existing shift.");
                    return false;
                }
            }
        }

        // Check if start-time and end-time are in 30-minute increments
        if (!isValidTimeIncrement()) {
            System.out.println("Error: Invalid time increments. Please use 30-minute increments.");
            return false;
        }

        return true;
    }

    public boolean canCancelShift() {
        // Check if there are any appointments in the shift
        if (hasAppointments()) {
            // Check if any appointment is in a status that allows cancellation (e.g., not approved)
            for (Appointment appointment : shiftAppointments) {
                if (!appointment.getStatus().equals("Approved")) {
                    return true; // Doctor can cancel the shift
                }
            }
            return false; // All appointments are approved, doctor cannot cancel the shift
        } else {
            // No appointments, doctor can cancel the shift
            return true;
        }
    }

    private boolean hasAppointments() {
        return shiftAppointments != null && !shiftAppointments.isEmpty();
    }
    public List<Appointment> getShiftAppointments(){
        return shiftAppointments;
    }

    public void generateShiftAppointments(){
        //create a list of appointments available within the shift
        //associate with the doctor of the shift, patient is initially null but will be assigned when booked

        java.time.LocalTime aStart = java.time.LocalTime.parse(this.getStartTime());
        java.time.LocalTime aEnd;
        while(aStart.isBefore(java.time.LocalTime.parse(this.getEndTime()))){
            aEnd = aStart.plusMinutes(30);
            //add the details of the appointment now
            //increment start and end at the end of the loop
            Appointment a = new Appointment();
            a.setDoctor(doctor);
            a.setDoctorID(doctorID);
            a.setDate(this.getDate());
            a.setStartTime(aStart.toString());
            a.setEndTime(aEnd.toString());
            a.setPatient(new Patient());
            String appointmentId = appointmentsRef.push().getKey();
            a.setAppointmentID(appointmentId);
            a.setStatus("Approved");
            a.setSpecialty(doctor.getSpecialties());

            appointmentsRef.child(appointmentId).setValue(a);
            shiftAppointments.add(a);
            Log.d("info","bookable appt created: "+a.getDate()+" "+a.getStartTime()+" with doctor: "+a.getDoctor().getEmployeeNumber());


            //bookableAppointmentList.add(a);

            aStart = aEnd; //next appointment starts at the end of the previous one
        }
    }
}
