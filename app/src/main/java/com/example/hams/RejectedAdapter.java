package com.example.hams;

import static com.example.hams.sendEmail.sendingEmail;

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

import javax.mail.MessagingException;

public class RejectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //ADAPTER FOR APPROVED USERS
    DatabaseReference usersRef = MainActivity.usersRef;
    Context context;
    //list of patients to display
    List<User> rejectedList = AdminPending.rejectedUsers;
    List<User> approvedList = AdminPending.approvedUsers;

    public RejectedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Info","User type at position "+position+": "+rejectedList.get(position).getType());
        // Determine the item type based on your data
        if (rejectedList.get(position).getType().equals("Patient")) {
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
            itemView = LayoutInflater.from(context).inflate(R.layout.rejected_patient,parent,false);
            return new MyViewHolder(itemView);
        } else{
            //inflate doctor
            itemView = LayoutInflater.from(context).inflate(R.layout.rejected_doctor,parent,false);
            return new DoctorViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("info","holder type: "+holder.getClass());
        if(holder instanceof MyViewHolder){
            Patient p = (Patient)rejectedList.get(position);
            //patient


            //set the text to match the item from the list passed in
            ((MyViewHolder) holder).first.setText(p.getFirstName());
            ((MyViewHolder) holder).last.setText(p.getLastName());
            ((MyViewHolder) holder).email.setText(p.getUserName());
            ((MyViewHolder) holder).address.setText(p.getAddress());
            ((MyViewHolder) holder).phoneNumber.setText(p.getPhoneNo());
            ((MyViewHolder) holder).healthCard.setText(p.getHealthCard());

            ((MyViewHolder) holder).accept.setText("Accept");

            //eventlistener for patient buttons
            ((MyViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                User user = rejectedList.get(position);

                Query fbUser = usersRef.orderByChild("userName").equalTo(user.getUserName());
                // approve
                fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Retrieve the specific user node
                            DatabaseReference userRef = userSnapshot.getRef();
                            // Update the user's information
                            userRef.child("status").setValue("Approved");
                            // sending email to notify of status change
                            /*try {
                                sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }*/
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
            }
        });
        } else if(holder instanceof DoctorViewHolder){
            Log.d("info","holder type (in doctor section): "+holder.getClass());
            Doctor d = (Doctor)rejectedList.get(position);
            //set the text to match the item from the list passed in
            ((DoctorViewHolder) holder).first.setText(d.getFirstName());
            ((DoctorViewHolder) holder).last.setText(d.getLastName());
            ((DoctorViewHolder) holder).email.setText(d.getUserName());
            ((DoctorViewHolder) holder).address.setText(d.getAddress());
            ((DoctorViewHolder) holder).phoneNumber.setText(d.getPhoneNumber());
            ((DoctorViewHolder) holder).employeeNumber.setText(d.getEmployeeNumber());
            ((DoctorViewHolder) holder).specialties.setText(d.getSpecialties());

            ((DoctorViewHolder) holder).accept.setText("Accept");
            ((DoctorViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                User user = rejectedList.get(position);

                Query fbUser = usersRef.orderByChild("userName").equalTo(user.getUserName());
                // approve
                fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Retrieve the specific user node
                            DatabaseReference userRef = userSnapshot.getRef();
                            // Update the user's information
                            userRef.child("status").setValue("Approved");
                            // sending email to notify of status change
                            /*try {
                                sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }*/
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors here
                    }
                });
            }
        });
        } else{
            Log.d("info","NEITHER PATIENT NOR DOCTOR??");
        }





    }

    @Override
    public int getItemCount() {
        return rejectedList.size();
    }
}
