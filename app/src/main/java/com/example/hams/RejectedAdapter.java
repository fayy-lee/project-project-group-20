package com.example.hams;

import android.content.Context;
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

public class RejectedAdapter extends RecyclerView.Adapter<MyViewHolder>{
    //ADAPTER FOR APPROVED USERS
    DatabaseReference usersRef = MainActivity.usersRef;
    Context context;
    //list of patients to display
    List<Patient> rejectedPatientList = AdminPending.rejectedPatients;
    List<Patient> approvedPatientList = AdminPending.approvedPatients;

    public RejectedAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a viewholder to store all the instances of request_patient views
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rejected_patient,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the text to match the item from the list passed in
        holder.first.setText(rejectedPatientList.get(position).getFirstName());
        holder.last.setText(rejectedPatientList.get(position).getLastName());
        holder.email.setText(rejectedPatientList.get(position).getUserName());
        holder.address.setText(rejectedPatientList.get(position).getAddress());
        holder.phoneNumber.setText(rejectedPatientList.get(position).getPhoneNo());
        holder.healthCard.setText(rejectedPatientList.get(position).getHealthCard());

        holder.accept.setText("Accept");

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                Patient patient = rejectedPatientList.get(position);

                Query fbPatient = usersRef.orderByChild("userName").equalTo(patient.getUserName());
                // approve
                fbPatient.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Retrieve the specific user node
                            DatabaseReference userRef = userSnapshot.getRef();
                            // Update the user's information
                            userRef.child("status").setValue("Approved");
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return rejectedPatientList.size();
    }
}
