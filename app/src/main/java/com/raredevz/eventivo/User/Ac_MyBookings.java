package com.raredevz.eventivo.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Booking;
import com.raredevz.eventivo.Helper.BookingStatus;
import com.raredevz.eventivo.Helper.PaymentsAdapter;
import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ac_MyBookings extends AppCompatActivity implements PaymentsAdapter.ItemClickListener{

    DatabaseReference dref;
    ArrayList<Booking> bookings;
    RecyclerView ryPayments;

    boolean eligibleForFeedback=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);
        dref= FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("My Bookings");
        ryPayments=findViewById(R.id.ry_payments);
        LinearLayoutManager nlayoutManager = new LinearLayoutManager(this);
        nlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryPayments.setLayoutManager(nlayoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_ry_venue));
        ryPayments.addItemDecoration(dividerItemDecoration);


        dref.child("Time_Slots").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookings=new ArrayList<>();
                for (DataSnapshot city:snapshot.getChildren()){
                    for (DataSnapshot v_id:city.getChildren()){
                      for (DataSnapshot dates:v_id.getChildren()){
                          for (DataSnapshot slots:dates.getChildren()){
                              Booking booking=slots.getValue(Booking.class);
                            if (booking.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                                bookings.add(booking);
                            }
                          }
                      }
                    }
                }
                PaymentsAdapter adapter=new PaymentsAdapter(Ac_MyBookings.this,bookings);
                adapter.setClickListener(Ac_MyBookings.this::onItemClick);
                ryPayments.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(View view, Booking booking) {
        if(booking.getStatus().equals(BookingStatus.Approved)){
            // Toast.makeText(this, booking.getPayment(), Toast.LENGTH_SHORT).show();
           // DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
            dref.child("Feedback").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(Ac_MyBookings.this, ""+snapshot.toString(), Toast.LENGTH_SHORT).show();
                    if (snapshot.hasChild(booking.getVenueId())){
                        if (snapshot.child(booking.getVenueId()).hasChild(FirebaseAuth.getInstance().getUid())){
                            AlertMessage.showMessage(Ac_MyBookings.this,"You can submit Feedback for this venue only once.");
                        }else {
                            Intent intent=new Intent(getApplicationContext(),Ac_Feedback.class);
                            intent.putExtra("booking",booking);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent=new Intent(getApplicationContext(),Ac_Feedback.class);
                        intent.putExtra("booking",booking);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //Toast.makeText(Ac_MyBookings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            AlertMessage.showMessage(Ac_MyBookings.this,"You can submit feedback after Approval of Booking!");
        }
    }
}