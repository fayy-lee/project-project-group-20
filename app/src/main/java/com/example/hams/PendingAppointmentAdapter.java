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

public class PendingAppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder> {
    List<Appointment> upcompingAppointmentsList = UpcomingAppointments.upcomingAppointmentList;
    Context context;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;

    public PendingAppointmentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pending_appointment,parent,false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment a = upcompingAppointmentsList.get(position);

        holder.date.setText(a.getDate());
        holder.startTime.setText(a.getStartTime());
        holder.patient.setText(a.getPatient().getFirstName() + " " + a.getPatient().getLastName());

        holder.accept.setText("Confirm");
        holder.reject.setText("Reject");

        //set event listeners for buttons
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFO","accept button clicked");
                approveAppointment(holder.getBindingAdapterPosition());
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectAppointment(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcompingAppointmentsList.size();
    }

    //method to approve status in firebase
    public void approveAppointment(int position){
        Appointment appointment = upcompingAppointmentsList.get(position);

        //find the specific appointment by its ID
        Query appointmentsQuery = appointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID());
        Log.d("INFO", "Appointment for id: "+appointment.getAppointmentID());
        appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.d("INFO","nothing in the query");
                }
                Log.d("INFO","in on data change method");
                for(DataSnapshot appointmentSnapshot : snapshot.getChildren()){
                    //reference the specific appointment
                    DatabaseReference appointmentRef = appointmentSnapshot.getRef();
                    appointmentRef.child("status").setValue("Approved");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void rejectAppointment(int position){
        Appointment appointment = upcompingAppointmentsList.get(position);

        //find the specific appointment by its ID
        Query appointmentsQuery = appointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID());
        Log.d("INFO", "Appointment for id: "+appointment.getAppointmentID());
        appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.d("INFO","nothing in the query");
                }
                Log.d("INFO","in on data change method");
                for(DataSnapshot appointmentSnapshot : snapshot.getChildren()){
                    Log.d("INFO","appointment status preop: "+appointment.getStatus());
                    //reference the specific appointment
                    DatabaseReference appointmentRef = appointmentSnapshot.getRef();
                    appointmentRef.child("status").setValue("Rejected");

                    Log.d("INFO","appointment status postop: "+appointment.getStatus());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
