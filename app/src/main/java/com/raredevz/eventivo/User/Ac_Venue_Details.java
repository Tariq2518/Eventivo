package com.raredevz.eventivo.User;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.raredevz.eventivo.Account.Ac_Login;
import com.raredevz.eventivo.Chat.ChatDetailsActivity;
import com.raredevz.eventivo.Chat.Friend;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.MapsActivity;
import com.raredevz.eventivo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;


public class Ac_Venue_Details extends AppCompatActivity {

    ImageView imgVenue;
    TextView tvVenueName, tvType, tvAddress, tvCity, tvVenueCapacity,tvVenueDistance,tvVenueCharges;

    CheckBox rbDj, rbDecoration;
    Venue venue;

    RatingBar rating;

    private FusedLocationProviderClient fusedLocationClient;

    double currRating=5.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue__details);

        venue = (Venue) getIntent().getSerializableExtra("venue");

        imgVenue = findViewById(R.id.imgVenue);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                //Toast.makeText(Ac_Venue_Details.this, "Location" + location.toString(), Toast.LENGTH_SHORT).show();
                                tvVenueDistance.setText((location.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM");
                             //   Toast.makeText(Ac_Venue_Details.this, "Distance " + (location.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM", Toast.LENGTH_SHORT).show();

                                // Logic to handle location object
                            }
                        }
                    });
        }


        tvVenueName = findViewById(R.id.tvVenueName);
        tvType = findViewById(R.id.tvVenueType);
        tvAddress = findViewById(R.id.tvVenueAddress);
        tvCity = findViewById(R.id.tvVenueCity);
        tvVenueCapacity = findViewById(R.id.tvVenueCapacity);
        tvVenueDistance=findViewById(R.id.tvVenueDistance);
        tvVenueCharges=findViewById(R.id.tvVenueCharges);

        rbDj = findViewById(R.id.rbDJ);
        rbDecoration = findViewById(R.id.rbDecoration);
        rating=findViewById(R.id.rating);



        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
        dref.child("Venues").child(venue.getCity()).child(venue.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("rating")){
                    currRating=snapshot.child("rating").getValue(double.class);
                    rating.setRating((float) currRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (venue != null) {
            Picasso.get().load(venue.getImageUrl()).into(imgVenue);
            tvVenueName.setText(venue.getName());
            tvType.setText(venue.getType());
            tvCity.setText(venue.getCity());
            tvVenueCharges.setText((venue.getCharges()/venue.getCapacity())+" per Head");
            tvVenueCapacity.setText(venue.getCapacity() + "");

            if (venue.isDj()) {
                rbDj.setChecked(true);
                rbDj.setVisibility(View.VISIBLE);
            } else {
                rbDj.setVisibility(View.GONE);
            }

            if (venue.isDecoration()) {
                rbDecoration.setChecked(true);
                rbDecoration.setVisibility(View.VISIBLE);
            } else {
                rbDecoration.setVisibility(View.GONE);
            }
            tvAddress.setText(getAddressFromLatLng(Ac_Venue_Details.this, new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude())));

        } else {
            onBackPressed();
        }
    }

    public void ShowOnMap(View view) {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        i.putExtra("location", venue.getLocation());
        i.putExtra("name", venue.getName());
        startActivity(i);
    }

    public void startChat(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent intent = new Intent(Ac_Venue_Details.this, ChatDetailsActivity.class);
            intent.putExtra("FRIEND", new Friend(venue.getManagerId(), venue.getName(), venue.getImageUrl()));
            intent.putExtra("fromHome", true);
            startActivity(intent);
        } else {
            Cons.otp_destination=Cons.otp_destination_user;
            startActivity(new Intent(getApplicationContext(), Ac_Login.class));
        }
    }

    public static String getAddressFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void bookVenue(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent i = new Intent(getApplicationContext(), Ac_Book_Venue.class);
            i.putExtra("venue", venue);
            startActivity(i);
        }else {
            Cons.otp_destination=Cons.otp_destination_user;
            startActivity(new Intent(getApplicationContext(), Ac_Login.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission ok. Do work.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    //Toast.makeText(Ac_Venue_Details.this, "Location" + location.toString(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(Ac_Venue_Details.this, "Distance " + (location.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM", Toast.LENGTH_SHORT).show();
                                    tvVenueDistance.setText((location.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM");
                                    // Logic to handle location object
                                }
                            }
                        });
            }
        }
    }
}