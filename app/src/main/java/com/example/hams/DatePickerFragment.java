package com.example.hams;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.
        String formattedMonth = String.format(Locale.getDefault(), "%02d", month + 1);
        String formattedDay = String.format(Locale.getDefault(), "%02d", day);

        String formattedDate = year + "-" + formattedMonth + "-" + formattedDay;
        ((UpcomingShifts)getActivity()).setSelectedDate(formattedDate);
    }
}