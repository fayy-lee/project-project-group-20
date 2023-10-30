package com.example.hams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApprovedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //ADAPTER FOR APPROVED USERS
    Context context;
    //list of patients to display
    List<User> approvedList = AdminPending.approvedUsers;

    public ApprovedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Info","User type at position "+position+": "+approvedList.get(position).getType());
        // Determine the item type based on your data
        if (approvedList.get(position).getType().equals("Patient")) {
            return 0;
        } else {
            return 1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        //create a viewholder to store all the instances of request_patient views
        if(viewType == 0){
            //inflate patient
            itemView = LayoutInflater.from(context).inflate(R.layout.approved_patient,parent,false);
            return new MyViewHolder(itemView);
        } else{
            //inflate doctor
            itemView = LayoutInflater.from(context).inflate(R.layout.approved_doctor,parent,false);
            return new DoctorViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){

            //patient
            Patient p = (Patient)approvedList.get(position);
            //set the text to match the item from the list passed in
            ((MyViewHolder) holder).first.setText(p.getFirstName());
            ((MyViewHolder) holder).last.setText(p.getLastName());
            ((MyViewHolder) holder).email.setText(p.getUserName());
            ((MyViewHolder) holder).address.setText(p.getAddress());
            ((MyViewHolder) holder).phoneNumber.setText(p.getPhoneNo());
            ((MyViewHolder) holder).healthCard.setText(p.getHealthCard());
        } else{
            Doctor d = (Doctor)approvedList.get(position);
            //set the text to match the item from the list passed in
            ((DoctorViewHolder) holder).first.setText(d.getFirstName());
            ((DoctorViewHolder) holder).last.setText(d.getLastName());
            ((DoctorViewHolder) holder).email.setText(d.getUserName());
            ((DoctorViewHolder) holder).address.setText(d.getAddress());
            ((DoctorViewHolder) holder).phoneNumber.setText(d.getPhoneNumber());
            ((DoctorViewHolder) holder).employeeNumber.setText(d.getEmployeeNumber());
            ((DoctorViewHolder) holder).specialties.setText(d.getSpecialties());
        }
    }

    @Override
    public int getItemCount() {
        return approvedList.size();
    }
}