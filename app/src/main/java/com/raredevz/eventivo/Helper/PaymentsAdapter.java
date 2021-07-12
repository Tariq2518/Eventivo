package com.raredevz.eventivo.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.R;

import java.util.List;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private List<Booking> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public PaymentsAdapter(Context context, List<Booking> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.li_bookings, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Booking booking = mData.get(position);
        holder.tvVenueName.setText("Name: "+booking.getVenueName());
        holder.tvDate.setText("Date: "+booking.getDate());
        holder.tvSlot.setText("Time: "+booking.getSlot());
        holder.tvCity.setText("City: "+booking.getVenueCity());
        holder.tvTrxId.setText("Trx. ID: "+booking.getTRXID());
        holder.tvPayment.setText("Payment: "+booking.getPayment());

        if (booking.getStatus().equals(BookingStatus.Pending)){
            holder.tvStatus.setText("Status: Pending");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.appColor));
        }else if (booking.getStatus().equals(BookingStatus.Declined)){
            holder.tvStatus.setText("Status: Declined");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_red));
        }else {
            holder.tvStatus.setText("Status: Approved");
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_green));
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvVenueName,tvDate,tvSlot,tvCity,tvStatus,tvTrxId,tvPayment;

        ViewHolder(View itemView) {
            super(itemView);
            tvVenueName = itemView.findViewById(R.id.tvVenueName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSlot = itemView.findViewById(R.id.tvSlot);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTrxId = itemView.findViewById(R.id.tvTrxId);
            tvPayment = itemView.findViewById(R.id.tvPayment);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, mData.get(getAdapterPosition()));
        }
    }

    // convenience method for getting data at click position
    Booking getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Booking booking);
    }
}
