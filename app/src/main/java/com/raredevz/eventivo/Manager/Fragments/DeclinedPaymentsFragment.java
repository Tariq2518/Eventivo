package com.raredevz.eventivo.Manager.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.Helper.Booking;
import com.raredevz.eventivo.Helper.BookingStatus;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.PaymentsAdapter;
import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeclinedPaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeclinedPaymentsFragment extends Fragment implements PaymentsAdapter.ItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeclinedPaymentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllPaymentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeclinedPaymentsFragment newInstance(String param1, String param2) {
        DeclinedPaymentsFragment fragment = new DeclinedPaymentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    DatabaseReference dref;
    ArrayList<Booking> bookings;
    RecyclerView ryPayments;
    View view;
    ContentLoadingProgressBar c_l_progress_bar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_all_payments, container, false);
        dref= FirebaseDatabase.getInstance().getReference();

        ryPayments=view.findViewById(R.id.ry_payments);
        c_l_progress_bar=view.findViewById(R.id.c_l_progress_bar);
        LinearLayoutManager nlayoutManager = new LinearLayoutManager(getContext());
        nlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryPayments.setLayoutManager(nlayoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.divider_ry_venue));
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
                               // Toast.makeText(getContext(), ""+slots.getKey(), Toast.LENGTH_SHORT).show();
                                if (booking.getManagerId().equals(FirebaseAuth.getInstance().getUid())){
                                    if (booking.getStatus().equals(BookingStatus.Declined))
                                        bookings.add(booking);
                                }
                            }
                        }
                    }
                }
                c_l_progress_bar.setVisibility(View.GONE);
                ImageView img=view.findViewById(R.id.img_no_dataFound);
                if (bookings.size()>0){
                    PaymentsAdapter adapter=new PaymentsAdapter(getContext(),bookings);
                    adapter.setClickListener(DeclinedPaymentsFragment.this::onItemClick);
                    ryPayments.setAdapter(adapter);
                    ryPayments.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);
                }else {
                    ryPayments.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    String decision="";
    @Override
    public void onItemClick(View view, Booking booking) {
        new AlertDialog.Builder(getContext())
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
        new AlertDialog.Builder(getContext())
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