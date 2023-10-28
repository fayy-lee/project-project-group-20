package com.example.hams;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    //reference the elements, so we can access them and put real data in later
    TextView first, last, email, address, phoneNumber, healthCard;
    Button accept, reject;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        //actually reference the views
        first = itemView.findViewById(R.id.firstname);
        last = itemView.findViewById(R.id.lastname);
        email = itemView.findViewById(R.id.email);
        address = itemView.findViewById(R.id.address);
        phoneNumber = itemView.findViewById(R.id.phoneNumber);
        healthCard = itemView.findViewById(R.id.healthCard);

        //reference buttons too, so they'll have text on them lol
        accept = itemView.findViewById(R.id.accept);
        reject = itemView.findViewById(R.id.reject);

    }
}
