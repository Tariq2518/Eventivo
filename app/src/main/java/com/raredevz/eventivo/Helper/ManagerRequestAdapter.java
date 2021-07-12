package com.raredevz.eventivo.Helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManagerRequestAdapter extends RecyclerView.Adapter<ManagerRequestAdapter.ViewHolder> {

    private List<Manager> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public ManagerRequestAdapter(Context context, List<Manager> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ly_manager_request, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Manager manager = mData.get(position);
        holder.tvManagerName.setText(manager.getName());
        holder.tvManagerContact.setText(manager.getContact());
        holder.tvManagerEmail.setText(manager.getEmail());
        Picasso.get().load(manager.getImageUrl()).into(holder.imgManager);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvManagerName, tvManagerContact,tvManagerEmail;
        ImageView imgManager;

        ViewHolder(View itemView) {
            super(itemView);
            tvManagerName = itemView.findViewById(R.id.tvManagerName);
            tvManagerContact =itemView.findViewById(R.id.tvManagerContact);
            tvManagerEmail=itemView.findViewById(R.id.tvManagerEmail);
            imgManager =itemView.findViewById(R.id.img_manager);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, mData.get(getAdapterPosition()));
        }
    }

    // convenience method for getting data at click position
    Manager getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Manager manager);
    }
}
