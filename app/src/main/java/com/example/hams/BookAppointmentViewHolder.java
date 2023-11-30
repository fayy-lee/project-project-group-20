package com.example.hams;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookAppointmentViewHolder extends RecyclerView.ViewHolder {
    //reference elements from appointment
    TextView date, doctorFirst, doctorLast, specialty, startTime, endTime;
    Button book;
    public BookAppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        //actually reference the views
        date = itemView.findViewById(R.id.date);
        doctorFirst = itemView.findViewById(R.id.doctorFirst);
        doctorLast = itemView.findViewById(R.id.doctorLast);
        specialty = itemView.findViewById(R.id.specialty);
        endTime = itemView.findViewById(R.id.endTime);
        startTime = itemView.findViewById(R.id.startTime);

        //reference buttons too, so they'll have text on them
        book = itemView.findViewById(R.id.book);

    }

}
