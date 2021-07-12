package com.raredevz.eventivo.Manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.raredevz.eventivo.Helper.Booking;
import com.raredevz.eventivo.Helper.BookingStatus;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.PaymentsAdapter;
import com.raredevz.eventivo.Helper.ViewPagerAdapter;
import com.raredevz.eventivo.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Ac_Payments extends AppCompatActivity implements PaymentsAdapter.ItemClickListener{

    DatabaseReference dref;
    ArrayList<Booking> bookings;
    RecyclerView ryPayments;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        dref= FirebaseDatabase.getInstance().getReference();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

//        ryPayments=findViewById(R.id.ry_payments);
//        LinearLayoutManager nlayoutManager = new LinearLayoutManager(this);
//        nlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        ryPayments.setLayoutManager(nlayoutManager);
//        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_ry_venue));
//        ryPayments.addItemDecoration(dividerItemDecoration);
//
//        dref.child("Time_Slots").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                bookings=new ArrayList<>();
//                for (DataSnapshot city:snapshot.getChildren()){
//                    for (DataSnapshot v_id:city.getChildren()){
//                      for (DataSnapshot dates:v_id.getChildren()){
//                          for (DataSnapshot slots:dates.getChildren()){
//                              Booking booking=slots.getValue(Booking.class);
//                              Toast.makeText(Ac_Payments.this, ""+slots.getKey(), Toast.LENGTH_SHORT).show();
//                            if (booking.getManagerId().equals(FirebaseAuth.getInstance().getUid())){
//                                if (booking.getStatus().equals(BookingStatus.Pending))
//                                    bookings.add(booking);
//                            }
//                          }
//                      }
//                    }
//                }
//                PaymentsAdapter adapter=new PaymentsAdapter(Ac_Payments.this,bookings);
//                adapter.setClickListener(Ac_Payments.this::onItemClick);
//                ryPayments.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    String decision="";
    @Override
    public void onItemClick(View view, Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Trx ID: "+booking.getTRXID()+"\nPayment: "+booking.getPayment())
                .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decision="Approved";
                        dialog.dismiss();
                        showAlert(booking);
                    }
                }).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decision="Declined";
                        dialog.dismiss();
                        showAlert(booking);
                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    void showAlert(Booking booking){
        new AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("Are you sure on your decision "+decision+" about "+booking.getTRXID()+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (decision.equals("Declined"))
                            booking.setStatus(BookingStatus.Declined);
                        else if (decision.equals("Approved"))
                            booking.setStatus(BookingStatus.Approved);

                        dref.child(Cons.node_time_slots).child(booking.getVenueCity())
                                .child(booking.getVenueId())
                                .child(booking.getDate())
                                .child(booking.getSlot())
                                .setValue(booking);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}