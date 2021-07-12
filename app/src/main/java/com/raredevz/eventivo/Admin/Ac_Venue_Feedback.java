package com.raredevz.eventivo.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ac_Venue_Feedback extends AppCompatActivity {

    ListView lvVenueFeedback;
    ArrayList<Venue> venuesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_feedback_admin);

        lvVenueFeedback=findViewById(R.id.lvVenueFeedback);


        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
        dref.child("Venues").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                venuesList=new ArrayList<>();
                for (DataSnapshot city:snapshot.getChildren()){
                    for (DataSnapshot v:city.getChildren()){
                        Venue venue=v.getValue(Venue.class);
                        venue.setId(v.getKey());
                        venuesList.add(venue);
                    }
                }
                venueFeedBackAdapter adapter=new venueFeedBackAdapter();
                lvVenueFeedback.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class  venueFeedBackAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return venuesList.size();
        }

        @Override
        public Object getItem(int position) {
            return venuesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(Ac_Venue_Feedback.this).inflate(R.layout.li_venue_feedback_admin,parent,false);
                holder.ratingBar=convertView.findViewById(R.id.rating);
                holder.tvName=convertView.findViewById(R.id.tvVenueName);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(venuesList.get(position).getName());
            holder.ratingBar.setRating((float)venuesList.get(position).getRating() );
            return convertView;
        }
        class ViewHolder{
            RatingBar ratingBar;
            TextView tvName;
        }
    }
}