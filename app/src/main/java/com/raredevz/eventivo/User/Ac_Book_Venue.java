package com.raredevz.eventivo.User;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Booking;
import com.raredevz.eventivo.Helper.BookingStatus;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Ac_Book_Venue extends AppCompatActivity {


    Button btnSelectDate,btnSelectSlot,btnBookVenue,btnEnterCapacity;
    Venue venue;

    ProgressDialog progressDialog;

    String selected_date="",selected_slot="";
    boolean slot1,slot2,retrieved=false;
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();

    int calculatedCharges=0;

    EditText txtEnterTrxId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__venue);

        btnSelectDate=findViewById(R.id.btnSelectDate);
        btnSelectSlot=findViewById(R.id.btnSelectSlot);
        btnBookVenue=findViewById(R.id.btnBookVenue);
        btnEnterCapacity=findViewById(R.id.btnEnterCapacity);
        btnBookVenue.setVisibility(View.INVISIBLE);

        txtEnterTrxId=findViewById(R.id.txtEnterTrxId);

        venue=(Venue) getIntent().getSerializableExtra("venue");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Please wait");

        slot1=slot2=true;

        txtEnterTrxId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0)
                    btnBookVenue.setVisibility(View.VISIBLE);
                else
                    btnBookVenue.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

    private void updateLabel() {

        String myFormat = "EEE-MMMdd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selected_date=sdf.format(myCalendar.getTime());
        btnSelectDate.setText(selected_date);

        progressDialog.show();


        dref.child(Cons.node_time_slots).child(venue.getCity()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if (snapshot.hasChild(venue.getId())){
                        if (snapshot.child(venue.getId()).hasChild(selected_date)){
                            if (snapshot.child(venue.getId()).child(selected_date).hasChild("slot1")){
                                slot1=false;
                            }
                            if (snapshot.child(venue.getId()).child(selected_date).hasChild("slot2")){
                                slot2=false;
                            }
                        }
                    }
                }
                progressDialog.dismiss();
                if (!retrieved) {
                    showSlotsDialog();
                    retrieved=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void selectDate(View view) {
       DatePickerDialog dialog= new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
       dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
       dialog.show();
    }

    private void showSlotsDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.ly_venue_slots);
        Button btnSlot1=dialog.findViewById(R.id.btnSlot1);
        Button btnSlot2=dialog.findViewById(R.id.btnSlot2);

        if (slot1){
            btnSlot1.setBackground(getDrawable(R.drawable.bg_btn));
            btnSlot1.setEnabled(true);
        }else {
            btnSlot1.setBackground(getDrawable(R.drawable.bg_btn_disabled));
            btnSlot1.setEnabled(false);
        }
        if (slot2){
            btnSlot2.setBackground(getDrawable(R.drawable.bg_btn));
            btnSlot2.setEnabled(true);
        }else {
            btnSlot2.setBackground(getDrawable(R.drawable.bg_btn_disabled));
            btnSlot2.setEnabled(false);
        }

        dialog.show();

        btnSlot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnterCapacity.setVisibility(View.VISIBLE);
                btnSelectSlot.setVisibility(View.VISIBLE);
              // btnBookVenue.setVisibility(View.VISIBLE);
                btnSelectSlot.setText("12 PM - 4 PM");
                selected_slot="slot1";
                dialog.dismiss();
                if (slot1){

                }

            }
        });
        btnSlot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnterCapacity.setVisibility(View.VISIBLE);
                btnSelectSlot.setVisibility(View.VISIBLE);
              //  btnBookVenue.setVisibility(View.VISIBLE);
                btnSelectSlot.setText("6 PM - 10 PM");
                selected_slot="slot2";
                dialog.dismiss();
                if (slot2){

                }

            }
        });
    }

    public void selectSlot(View view) {
        showSlotsDialog();
    }

    Booking booking;
    public void bookVenue(View view) {

        if (selected_slot.equals("")){
            AlertMessage.showMessage(this,"Please select an appropriate Time Slot!");
            return;
        }
        if (calculatedCharges==0){
            AlertMessage.showMessage(this,"Enter valid Required Capacity to book the Venue!");
            return;
        }
        booking=new Booking();
        booking.setDate(btnSelectDate.getText().toString());
        booking.setManagerId(venue.getManagerId());
        booking.setVenueId(venue.getId());
        booking.setUserId(FirebaseAuth.getInstance().getUid());
        booking.setSlot(selected_slot);
        booking.setRequiredCapacity(txtCapacityReq.getText().toString());
        booking.setTRXID(txtEnterTrxId.getText().toString());
        booking.setVenueName(venue.getName());
        booking.setStatus(BookingStatus.Pending);
        booking.setVenueCity(venue.getCity());
        booking.setPayment(calculatedCharges+"");

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        if (selected_slot.equals("slot1"))
            builder.setMessage("Are you really want to book this venue on "+selected_date+" for the time 12PM to 4PM ?");
        if (selected_slot.equals("slot2"))
            builder.setMessage("Are you really want to book this venue on "+selected_date+" for the time 6PM to 10PM ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dref.child(Cons.node_time_slots).child(venue.getCity()).child(venue.getId()).child(selected_date).child(selected_slot).setValue(booking);
                startActivity(new Intent(getApplicationContext(),Ac_Home.class));
                finish();
                Toast.makeText(Ac_Book_Venue.this, "Venue Booked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    EditText txtCapacityReq;
    public void EnterRequiredCapacity(View view) {
        Dialog dialog=new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ly_enter_required_capacity);
        txtCapacityReq=dialog.findViewById(R.id.txtRequiredCapacity);
        TextView tvShowCharges=dialog.findViewById(R.id.tvShowCharges);
        Button btnSubmit=dialog.findViewById(R.id.btnSubmitRequiredCapacity);
        txtCapacityReq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (Integer.parseInt(txtCapacityReq.getText().toString())>venue.getCapacity()){
                        tvShowCharges.setText("Your desired sitting capacity is greater than the capacity of Venue, please enter less or choose other venue");
                        btnSubmit.setEnabled(false);
                    }else {
                        btnSubmit.setEnabled(true);
                        calculatedCharges=((venue.getCharges()/venue.getCapacity())*Integer.parseInt(txtCapacityReq.getText().toString()));
                        tvShowCharges.setText("Your total charges would be "+calculatedCharges);
                    }
                }catch (Exception e){
                    tvShowCharges.setText("Enter valid capacity!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtCapacityReq.getText().toString())){
                    Toast.makeText(Ac_Book_Venue.this, "Enter valid required capacity", Toast.LENGTH_SHORT).show();
                }else{
                    btnEnterCapacity.setText(txtCapacityReq.getText().toString()+" Persons");
                    dialog.dismiss();
                    txtEnterTrxId.setVisibility(View.VISIBLE);
                    AlertMessage.showMessage(Ac_Book_Venue.this,"Please transfer an amount of pkr \""+calculatedCharges+"\" on the Account \""+venue.getAcctNumber()+"\" and Submit the Transaction ID to confirm your Slot!");
                }
            }
        });
    }
}