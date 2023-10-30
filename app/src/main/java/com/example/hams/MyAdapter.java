package com.example.hams;

import static com.example.hams.sendEmail.sendingEmail;

import android.content.Context;
import android.provider.ContactsContract;
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

import javax.mail.MessagingException;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    //ADAPTER FOR PENDING USERS
    //get database reference from MainActivity
    DatabaseReference usersRef = MainActivity.usersRef;

    Context context;
    //list of patients to display
    List<Patient> pendingPatientList = AdminPending.pendingPatients;
    List<Patient> approvedPatientList = AdminPending.approvedPatients;
    List<Patient> rejectedPatientList = AdminPending.rejectedPatients;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a viewholder to store all the instances of request_patient views
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.request_patient,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the text to match the item from the list passed in
        holder.first.setText(pendingPatientList.get(position).getFirstName());
        holder.last.setText(pendingPatientList.get(position).getLastName());
        holder.email.setText(pendingPatientList.get(position).getUserName());
        holder.address.setText(pendingPatientList.get(position).getAddress());
        holder.phoneNumber.setText(pendingPatientList.get(position).getPhoneNo());
        holder.healthCard.setText(pendingPatientList.get(position).getHealthCard());

        holder.accept.setText("Accept");
        holder.reject.setText("Reject");


        //now for the event listeners
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Info", "accept button clicked");
                //now change status
                // Access the data associated with the clicked item
                int position = holder.getBindingAdapterPosition(); //position of the element removed

                Patient patient = pendingPatientList.get(position);
                //find the patient in firebase with the same email address
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
                            // sending the email to notify
                            try {
                                sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                Patient patient = pendingPatientList.get(position);

                Query fbPatient = usersRef.orderByChild("userName").equalTo(patient.getUserName());

                fbPatient.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot userSnapshot : snapshot.getChildren()){
                            DatabaseReference userRef = userSnapshot.getRef();
                            userRef.child("status").setValue("Rejected");
                            try {
                                sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingPatientList.size();
    }
}
