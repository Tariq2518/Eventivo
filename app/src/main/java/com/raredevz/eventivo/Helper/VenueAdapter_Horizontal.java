package com.raredevz.eventivo.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VenueAdapter_Horizontal extends RecyclerView.Adapter<VenueAdapter_Horizontal.ViewHolder>  implements Filterable {

    private List<Venue> mData;
    private List<Venue> originalList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private  Context mContext;

    // data is passed into the constructor
    public VenueAdapter_Horizontal(Context context, List<Venue> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.originalList = data;
        this.mContext=context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ly_venues, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Venue venue = mData.get(position);
        holder.tvVenueName.setText(venue.getName());
        holder.tvVenueCapacity.setText(venue.getCapacity()+"");
        Picasso.get().load(venue.getImageUrl()).into(holder.imgVenue);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvVenueName,tvVenueCapacity;
        ImageView imgVenue;

        ViewHolder(View itemView) {
            super(itemView);
            tvVenueName = itemView.findViewById(R.id.tvVenueName);
            tvVenueCapacity=itemView.findViewById(R.id.tvVenueCapacity);
            imgVenue=itemView.findViewById(R.id.imgVenue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, mData.get(getAdapterPosition()));
        }
    }

    public void filter(String text) {
        List<Venue> itemsCopy=mData;
        mData.clear();
        if(text.isEmpty()){
            mData.addAll(itemsCopy);
        } else{
            text = text.toLowerCase();
            for(Venue item: itemsCopy){
                if(item.name.toLowerCase().contains(text) || item.city.toLowerCase().contains(text)){
                    mData.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // convenience method for getting data at click position
    Venue getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Venue venue);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData = (List<Venue>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Venue> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Venue> getFilteredResults(String constraint) {
        List<Venue> results = new ArrayList<>();

        for (Venue item : originalList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
