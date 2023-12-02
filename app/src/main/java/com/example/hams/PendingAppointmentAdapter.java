package com.example.hams;

import static com.example.hams.MainActivity.appointmentsRef;
import static com.example.hams.MainActivity.usersRef;
import static com.example.hams.UpcomingAppointments.approvedAppointmentList;
import static com.example.hams.UpcomingAppointments.upcomingAppointmentList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PendingAppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder>{
    List<Appointment> upcompingAppointmentsList = UpcomingAppointments.upcomingAppointmentList;
    Context context;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    DatabaseReference shiftsRef = MainActivity.shiftRef;

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
        holder.firstName.setText(a.getPatient().getFirstName());
        holder.lastName.setText(a.getPatient().getLastName());
        holder.email.setText(a.getPatient().getUserName());
        holder.address.setText(a.getPatient().getAddress());
        holder.phone.setText(a.getPatient().getPhoneNo());
        holder.healthCard.setText(a.getPatient().getHealthCard());

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
        //push the appointment to the Appointments section once it's confirmed
        Appointment appointment = upcompingAppointmentsList.get(position);
        appointment.setStatus("Approved");
        appointmentsRef.child(appointment.getAppointmentID()).setValue(appointment);
        Query shiftsQuery = shiftsRef.orderByChild("doctorID").equalTo(appointment.getDoctorID());
        shiftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot shiftSnapshot : snapshot.getChildren()){
                    //for each shift, now we search its shiftAppointments for the one with appointmentID
                    String shiftId = shiftSnapshot.getKey();
                    DatabaseReference shiftAppointmentsRef = shiftsRef.child(shiftId).child("shiftAppointments");
                    shiftAppointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //now change the status of the appointment
                            for(DataSnapshot shiftAppointmentSnapshot : snapshot.getChildren()){
                                //reference the specific appointment
                                DatabaseReference appointmentRef = shiftAppointmentSnapshot.getRef();

                                //change status, remove from bookable options
                                appointmentRef.child("status").setValue("Approved");

                                upcomingAppointmentList.remove(appointment);
                                notifyDataSetChanged();
                                approvedAppointmentList.add(appointment);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void rejectAppointment(int position){
        Appointment appointment = upcompingAppointmentsList.get(position);
        appointment.setStatus("Approved");
        Query shiftsQuery = shiftsRef.orderByChild("doctorID").equalTo(appointment.getDoctorID());
        shiftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot shiftSnapshot : snapshot.getChildren()){
                    //for each shift, now we search its shiftAppointments for the one with appointmentID
                    String shiftId = shiftSnapshot.getKey();
                    DatabaseReference shiftAppointmentsRef = shiftsRef.child(shiftId).child("shiftAppointments");
                    shiftAppointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //now change the status of the appointment
                            for(DataSnapshot shiftAppointmentSnapshot : snapshot.getChildren()){
                                //reference the specific appointment
                                DatabaseReference appointmentRef = shiftAppointmentSnapshot.getRef();

                                //change status, remove from bookable options
                                appointmentRef.child("status").setValue("Rejected");

                                upcomingAppointmentList.remove(appointment);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
