package com.example.hams;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class shiftviewholder extends RecyclerView.ViewHolder {
    //reference elements from appointment
    TextView date, startTime, endTime;
    Button cancel;
    public shiftviewholder(@NonNull View itemView) {
        super(itemView);
        //actually reference the views
        date = itemView.findViewById(R.id.date);

        startTime = itemView.findViewById(R.id.startTime);
        endTime = itemView.findViewById(R.id.endTime);
        //reference buttons too, so they'll have text on them lol
        cancel = itemView.findViewById(R.id.cancel);


    }
}
