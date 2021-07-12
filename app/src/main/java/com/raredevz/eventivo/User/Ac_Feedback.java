package com.raredevz.eventivo.User;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.raredevz.eventivo.Helper.Booking;
import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Ac_Feedback extends AppCompatActivity {

    RadioGroup rg1,rg2,rg3,rg4,rg5,rg6,rg7,rg8,rg9;
    TextView tvVenueName;Booking booking;
    HashMap<String,String> feedback=new HashMap<>();
    double currRating=5.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvVenueName=findViewById(R.id.tvVenueName);
        booking=(Booking)getIntent().getSerializableExtra("booking");

        tvVenueName.setText(booking.getVenueName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

        rg1=findViewById(R.id.R_G1);
        rg2=findViewById(R.id.R_G2);
        rg3=findViewById(R.id.R_G3);
        rg4=findViewById(R.id.R_G4);
        rg5=findViewById(R.id.R_G5);
        rg6=findViewById(R.id.R_G6);
        rg7=findViewById(R.id.R_G7);
        rg8=findViewById(R.id.R_G8);
        rg9=findViewById(R.id.R_G9);

        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
        dref.child("Venues").child(booking.getVenueCity()).child(booking.getVenueId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("rating")){
                    currRating=snapshot.child("rating").getValue(double.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("TasteofFood",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("QualityofFood",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("ManuVariety",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("ValuefortheMoney",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("OrderAccuracy",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("Ambiance",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("OverallSatisfaction",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });
        rg8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("SpeedofService",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });

        rg9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                feedback.put("RecommendToOthers",(String) ((RadioButton)findViewById(checkedId)).getTag());
            }
        });



    }

    public void submitFeedback(View view) {
        if (feedback.keySet().size()<9){
            Toast.makeText(this, "Select all", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
            dref.child("Feedback").child(booking.getVenueId()).child(FirebaseAuth.getInstance().getUid()).setValue(feedback);
            double rating=0.0;
            if (feedback.get("RecommendToOthers").equals("Probablyyes")){
                rating=3.75;
            }else if (feedback.get("RecommendToOthers").equals("Probablyno")){
                rating=2.5;
            }else if (feedback.get("RecommendToOthers").equals("Definitelyyes")){
                rating=5.0;
            }else if (feedback.get("RecommendToOthers").equals("Definitelyno")){
                rating=1.25;
            }
            currRating=(currRating+rating)/2;
            dref.child("Venues").child(booking.getVenueCity()).child(booking.getVenueId()).child("rating").setValue(currRating);
            Toast.makeText(this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}