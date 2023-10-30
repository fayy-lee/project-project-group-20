package com.example.hams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApprovedAdapter extends RecyclerView.Adapter<MyViewHolder>{
    //ADAPTER FOR APPROVED USERS
    Context context;
    //list of patients to display
    List<Patient> approvedPatientList = AdminPending.approvedPatients;

    public ApprovedAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a viewholder to store all the instances of request_patient views
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.approved_patient,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the text to match the item from the list passed in
        holder.first.setText(approvedPatientList.get(position).getFirstName());
        holder.last.setText(approvedPatientList.get(position).getLastName());
        holder.email.setText(approvedPatientList.get(position).getUserName());
        holder.address.setText(approvedPatientList.get(position).getAddress());
        holder.phoneNumber.setText(approvedPatientList.get(position).getPhoneNo());
        holder.healthCard.setText(approvedPatientList.get(position).getHealthCard());

        Log.d("info", "in approvedAdapter, approvedPatientList size: "+approvedPatientList.size());

    }

    @Override
    public int getItemCount() {
        return approvedPatientList.size();
    }
}