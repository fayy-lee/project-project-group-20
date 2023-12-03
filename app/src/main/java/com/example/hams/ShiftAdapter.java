package com.example.hams;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<shiftviewholder> {
    List<Shift> shiftAppointments ;

    List<Shift> shiftList = UpcomingShifts.shiftList;
    Context context;

    /*public ShiftAdapter(List<Shift> shiftList, OnShiftClickListener onShiftClickListener) {
        this.shiftList = shiftList;
        this.onShiftClickListener = onShiftClickListener;
    }*/
    public ShiftAdapter(Context context, List<Shift> shiftList){
        this.context = context;
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public shiftviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_shift, parent, false);
        return new shiftviewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull shiftviewholder holder, int position) {
        Shift shift = shiftList.get(position);
        holder.date.setText(shift.getDate());
        holder.startTime.setText(shift.getStartTime());
        holder.endTime.setText((shift.getEndTime()));
        if (!shift.canCancelShift()) {
            holder.cancel.setEnabled(false);
            holder.cancel.setBackgroundColor(Color.GRAY); // Change the text color to gray if the doctor can't cancel the button
        } else {
            holder.cancel.setEnabled(true);
            holder.cancel.setTextColor(Color.BLACK); // Change back to the default text color
        }
        holder.cancel.setText("Cancel");

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    removeShiftAtPosition(currentPosition);
                }
            }
        });

    }
    private void removeShiftAtPosition(int position) {

        if (position < 0 || position >= shiftList.size()) {
            // Position is invalid, likely due to concurrent modifications or incorrect index
            Log.e("ShiftAdapter", "Attempted to remove item at invalid position: " + position);
            return;
        }

        Shift shiftToRemove = shiftList.get(position);

        // Remove from Firebase
        if (shiftToRemove.getShiftID() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Shifts").child(shiftToRemove.getShiftID());
            ref.removeValue().addOnSuccessListener(aVoid -> {
                // Double-check if the position is still valid
                if (position < shiftList.size()&& shiftToRemove.equals(shiftList.get(position))) {
                    // Remove from local list and update adapter
                    shiftList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, shiftList.size());
                } else {
                    Log.e("ShiftAdapter", "Position became invalid after Firebase deletion: " + position);
                }
            }).addOnFailureListener(e -> {
                // Log or handle the error
                Log.e("Firebase", "Error deleting shift", e);
            });
        } else {
            // If shift doesn't have an ID, it's not synced with Firebase
            shiftList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, shiftList.size());
        }
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    /*public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }

    public interface OnShiftClickListener {
        void onShiftClick(int position);
    }*/

    /*public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }

        public void bind(Shift shift) {
            textViewDate.setText("Date: " + shift.getDate());
            textViewTime.setText("Time: " + shift.getStartTime() + " - " + shift.getEndTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShiftClickListener != null) {
                        onShiftClickListener.onShiftClick(getAdapterPosition());
                    }
                }
            });
        }
    }*/
}