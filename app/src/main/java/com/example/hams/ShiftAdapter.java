package com.example.hams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private List<Shift> shiftList;
    private OnShiftClickListener onShiftClickListener;

    public ShiftAdapter(List<Shift> shiftList, OnShiftClickListener onShiftClickListener) {
        this.shiftList = shiftList;
        this.onShiftClickListener = onShiftClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shift shift = shiftList.get(position);
        holder.bind(shift);
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
        notifyDataSetChanged();
    }

    public interface OnShiftClickListener {
        void onShiftClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
    }
}