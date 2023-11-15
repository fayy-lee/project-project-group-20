package com.example.hams;

import android.os.Build;


import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalTime;
import org.threeten.bp.LocalDate;


public class Shift {
    private String date;
    private String startTime;
    private String endTime;

    public Shift(){

    }

    public Shift(String date, String startTime, String endTime) {
        new Shift();
        this.setDate(date);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
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
}
