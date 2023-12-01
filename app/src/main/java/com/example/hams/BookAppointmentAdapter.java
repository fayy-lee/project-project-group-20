package com.example.hams;

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

public class BookAppointmentAdapter extends RecyclerView.Adapter<BookAppointmentViewHolder> {
    List<Appointment> bookableAppointmentList = BookAppointments.bookableAppointmentList;
    Context context;
    DatabaseReference appointmentsRef = MainActivity.appointmentsRef;
    DatabaseReference usersRef = MainActivity.usersRef;

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
                bookAppointment(holder.getBindingAdapterPosition());
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //currently signed in user
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference userRef = usersRef.child(user.getUid());
        final Patient[] p = new Patient[1];
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p[0] = snapshot.getValue(Patient.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //find the specific appointment by its ID
        Query appointmentsQuery = appointmentsRef.orderByChild("appointmentID").equalTo(appointment.getAppointmentID());
        Log.d("info","appointmentID: "+appointment.getAppointmentID());
        appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.d("INFO","nothing in the query");
                }
                for(DataSnapshot appointmentSnapshot : snapshot.getChildren()){
                    //reference the specific appointment
                    DatabaseReference appointmentRef = appointmentSnapshot.getRef();
                    //appointment is more than an hour away
                    appointmentRef.child("status").setValue("Pending");
                    Patient patient = p[0];
                    if(appointment.getPatient() == null){
                        Log.d("info","patient is null...setting to patient");
                    }
                    appointment.setPatient(patient);
                    // Update the patient information in Firebase
                    appointmentRef.child("patient").setValue(patient);
                    appointmentRef.child("patientID").setValue(patient.getHealthCard());
                    Log.d("info","patient name: "+ appointment.getPatient().getFirstName());

                    notifyDataSetChanged();
                    upcomingAppointmentList.add(appointment);
                    for(Appointment a : upcomingAppointmentList){
                        if(a.getPatient() == null){
                            Log.d("info","patient is null ACCORDING TO BOOKAPPT");
                        }
                    }
                    bookableAppointmentList.remove(appointment);
                    notifyDataSetChanged();
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