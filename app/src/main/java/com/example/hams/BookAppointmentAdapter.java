package com.example.hams;

import static com.example.hams.MainActivity.usersRef;
import static com.example.hams.UpcomingAppointments.approvedAppointmentList;
import static com.example.hams.UpcomingAppointments.upcomingAppointmentList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookAppointmentAdapter extends RecyclerView.Adapter<BookAppointmentViewHolder> implements OnUserDataChangedListener {
    List<Appointment> bookableAppointmentList = BookAppointments.bookableAppointmentList;
    Context context;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    DatabaseReference usersRef = MainActivity.usersRef;
    DatabaseReference shiftsRef = MainActivity.shiftRef;
    Patient currentPatient;

    public BookAppointmentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BookAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.book_appointment, parent, false);
        return new BookAppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAppointmentViewHolder holder, int position) {
        Appointment a = bookableAppointmentList.get(position);

        holder.date.setText(a.getDate());
        holder.startTime.setText(a.getStartTime());
        holder.endTime.setText(a.getEndTime());
        holder.doctorLast.setText(a.getDoctor().getLastName());
        holder.doctorFirst.setText(a.getDoctor().getFirstName());
        holder.specialty.setText(a.getSpecialty());

        holder.book.setText("Book");

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("INFO", "BOOK BUTTON CLICKED");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                //currently signed in user
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference userRef = usersRef.child(user.getUid());
                getCurrentPatient(userRef, holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookableAppointmentList.size();
    }


    public void bookAppointment(int position){
        //find the appointment in firebase
        Appointment appointment = bookableAppointmentList.get(position);


        shiftsRef.orderByChild("doctorID").equalTo(appointment.getDoctorID()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                //set patient info
                                Patient patient = currentPatient;
                                appointment.setPatient(patient);
                                // Update the patient information in Firebase
                                appointmentRef.child("patient").setValue(patient);
                                appointmentRef.child("patientID").setValue(patient.getHealthCard());
                                Log.d("info","patient name: "+ appointment.getPatient().getFirstName());

                                //change status, remove from bookable options
                                appointmentRef.child("status").setValue("Pending");

                                bookableAppointmentList.remove(appointment);
                                notifyDataSetChanged();
                                upcomingAppointmentList.add(appointment);
                                Toast.makeText(context, "Appointment booked, pending doctor approval.",
                                        Toast.LENGTH_LONG).show();
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

    private void getCurrentPatient(DatabaseReference userRef, int position) {
        OnUserDataChangedListener listener = this;
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient p = snapshot.getValue(Patient.class);
                Log.d("info", "patient object from snapshot: " + p);
                // Invoke the callback with the updated Doctor object
                //send the doctor to the callback method
                listener.onUserDataChanged(p, position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
    public void onUserDataChanged(User user){
        //required method, but we really want the other one so we can pass the position along
        //currentPatient = (Patient) user;
    }
    public void onUserDataChanged(User user, int position){
        currentPatient = (Patient) user;
        bookAppointment(position);
    }
}