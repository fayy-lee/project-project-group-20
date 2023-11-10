package com.example.hams;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentViewHolder extends RecyclerView.ViewHolder {
    //reference elements from appointment
    TextView date, patient, startTime;
    Button accept, reject;
    public AppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        //actually reference the views
        date = itemView.findViewById(R.id.date);
        patient = itemView.findViewById(R.id.patient);
        startTime = itemView.findViewById(R.id.startTime);

        //reference buttons too, so they'll have text on them lol
        accept = itemView.findViewById(R.id.accept);
        reject = itemView.findViewById(R.id.reject);

    }

}
