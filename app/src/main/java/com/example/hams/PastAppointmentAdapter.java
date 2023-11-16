package com.example.hams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PastAppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder> {
    List<Appointment> approvedAppointmentList = UpcomingAppointments.approvedAppointmentList;
    Context context;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    List<Appointment> pastAppointmentList = UpcomingAppointments.pastAppointmentList;

    public PastAppointmentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.past_appointment,parent,false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment a = approvedAppointmentList.get(position);

        holder.date.setText(a.getDate());
        holder.startTime.setText(a.getStartTime());
        holder.firstName.setText(a.getPatient().getFirstName());
        holder.lastName.setText(a.getPatient().getLastName());
        holder.email.setText(a.getPatient().getUserName());
        holder.address.setText(a.getPatient().getAddress());
        holder.phone.setText(a.getPatient().getPhoneNo());
        holder.healthCard.setText(a.getPatient().getHealthCard());
    }

    @Override
    public int getItemCount() {
        return pastAppointmentList.size();
    }

}
