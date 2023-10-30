package com.example.hams;

import static com.example.hams.sendEmail.sendingEmail;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import javax.mail.MessagingException;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //ADAPTER FOR PENDING USERS
    //get database reference from MainActivity
    DatabaseReference usersRef = MainActivity.usersRef;

    Context context;
    //list of patients to display
    List<User> pendingList = AdminPending.pendingUsers;
    List<User> approvedList = AdminPending.approvedUsers;
    List<User> rejectedList = AdminPending.rejectedUsers;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Info","User type at position "+position+": "+pendingList.get(position).getType());
        // Determine the item type based on your data
        if (pendingList.get(position).getType().equals("Patient")) {
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
            itemView = LayoutInflater.from(context).inflate(R.layout.request_patient,parent,false);
            return new MyViewHolder(itemView);
        } else{
            //inflate doctor
            itemView = LayoutInflater.from(context).inflate(R.layout.pending_doctor,parent,false);
            return new DoctorViewHolder(itemView);
        }
    }

    //@Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){

            //patient
            Patient p = (Patient)pendingList.get(position);
            //set the text to match the item from the list passed in
            ((MyViewHolder) holder).first.setText(p.getFirstName());
            ((MyViewHolder) holder).last.setText(p.getLastName());
            ((MyViewHolder) holder).email.setText(p.getUserName());
            ((MyViewHolder) holder).address.setText(p.getAddress());
            ((MyViewHolder) holder).phoneNumber.setText(p.getPhoneNo());
            ((MyViewHolder) holder).healthCard.setText(p.getHealthCard());

            ((MyViewHolder) holder).accept.setText("Accept");
            ((MyViewHolder) holder).reject.setText("Reject");

            //event listeners for buttons (patient specific)
            //now for the event listeners
            ((MyViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Info", "accept button clicked");
                    //now change status
                    // Access the data associated with the clicked item
                    int position = holder.getBindingAdapterPosition(); //position of the element removed

                    User user = pendingList.get(position);
                    //find the patient in firebase with the same email address
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
                                // sending the email to notify
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

            ((MyViewHolder) holder).reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getBindingAdapterPosition();
                    User user = pendingList.get(position);

                    Query fbUser = usersRef.orderByChild("userName").equalTo(user.getUserName());

                    fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot userSnapshot : snapshot.getChildren()){
                                DatabaseReference userRef = userSnapshot.getRef();
                                userRef.child("status").setValue("Rejected");
                                /*
                                try {
                                    sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                                } catch (MessagingException e) {
                                    throw new RuntimeException(e);
                                }*/
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        } else{
            Doctor d = (Doctor)pendingList.get(position);
             //set the text to match the item from the list passed in
            ((DoctorViewHolder) holder).first.setText(d.getFirstName());
            ((DoctorViewHolder) holder).last.setText(d.getLastName());
            ((DoctorViewHolder) holder).email.setText(d.getUserName());
            ((DoctorViewHolder) holder).address.setText(d.getAddress());
            ((DoctorViewHolder) holder).phoneNumber.setText(d.getPhoneNumber());
            ((DoctorViewHolder) holder).employeeNumber.setText(d.getEmployeeNumber());
            ((DoctorViewHolder) holder).specialties.setText(d.getSpecialties());

            ((DoctorViewHolder) holder).accept.setText("Accept");
            ((DoctorViewHolder) holder).reject.setText("Reject");

            //now for the event listeners
            ((DoctorViewHolder) holder).accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Info", "accept button clicked");
                //now change status
                // Access the data associated with the clicked item
                int position = holder.getBindingAdapterPosition(); //position of the element removed

                User user = pendingList.get(position);
                //find the patient in firebase with the same email address
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
                            // sending the email to notify
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

        ((DoctorViewHolder) holder).reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                User user = pendingList.get(position);

                Query fbUser = usersRef.orderByChild("userName").equalTo(user.getUserName());

                fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot userSnapshot : snapshot.getChildren()){
                            DatabaseReference userRef = userSnapshot.getRef();
                            userRef.child("status").setValue("Rejected");
                            /*
                            try {
                                sendingEmail(User.getUserName(), "Account Status Change", "Your account status has changed.");
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        }




    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }
}
