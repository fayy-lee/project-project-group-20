package com.example.hams;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentViewHolder extends RecyclerView.ViewHolder {
    //reference elements from appointment
    TextView date, firstName, lastName, email, address, phone, healthCard, startTime;
    Button accept, reject;
    public AppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        //actually reference the views
        date = itemView.findViewById(R.id.date);
        firstName = itemView.findViewById(R.id.firstname);
        lastName = itemView.findViewById(R.id.lastname);
        email = itemView.findViewById(R.id.email);
        address = itemView.findViewById(R.id.address);
        phone = itemView.findViewById(R.id.phoneNumber);
        healthCard = itemView.findViewById(R.id.healthCard);
        startTime = itemView.findViewById(R.id.startTime);

        //reference buttons too, so they'll have text on them lol
        accept = itemView.findViewById(R.id.accept);
        reject = itemView.findViewById(R.id.reject);

    }

}
