package com.example.hams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<shiftviewholder> {

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

        shiftList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, shiftList.size());
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