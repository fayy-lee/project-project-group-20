package com.example.hams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
                //rejectAppointment(holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookableAppointmentList.size();
    }

}