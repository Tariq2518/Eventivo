package com.raredevz.eventivo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.Account.Ac_Manager_Login;
import com.raredevz.eventivo.Chat.Constants;
import com.raredevz.eventivo.Chat.MainActivity;
import com.raredevz.eventivo.Chat.SettingsAPI;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.Helper.VenueAdapter_Vertical;
import com.raredevz.eventivo.Manager.Ac_AddVanue;
import com.raredevz.eventivo.Manager.Ac_EditVenue;
import com.raredevz.eventivo.Manager.Ac_Payments;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Ac_Manager_Panel extends AppCompatActivity implements VenueAdapter_Vertical.ItemClickListener{

    NavigationView navigationView;

    FirebaseAuth firebaseAuth;
    FloatingActionButton fabAddVenue;
    DatabaseReference Dref;
    List<Venue> venueList;

    RecyclerView ry_my_venues;
    Manager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_panel);



        //region DrawerNavigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_Manager_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView=findViewById(R.id.nav_view);

        final View view =navigationView.getHeaderView(0);
        final Button btnAccount=view.findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Ac_Manager_Login.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.btnDrawerAddVenue){
                    startActivity(new Intent(getApplicationContext(), Ac_AddVanue.class));
                }else if (item.getItemId()==R.id.btnDrawerPayments){
                    startActivity(new Intent(getApplicationContext(), Ac_Payments.class));
                }else if (item.getItemId()==R.id.btnDrawerHome){
                    startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Manager_Panel.class));
                    finish();
                }
//                else if (item.getItemId()==R.id.btnDrawerBooking){
//                    startActivity(new Intent(getApplicationContext(), Ac_ManageBooking.class));
//                }
                else if (item.getItemId()==R.id.btnDrawerChat){
                    if (firebaseAuth.getCurrentUser()!=null){
                        try {
                            SettingsAPI set;
                            set = new SettingsAPI(com.raredevz.eventivo.Ac_Manager_Panel.this);
                            set.addUpdateSettings(Constants.PREF_MY_ID, FirebaseAuth.getInstance().getUid());
                            set.addUpdateSettings(Constants.PREF_MY_NAME, manager.getName());
                            set.addUpdateSettings(Constants.PREF_MY_DP, manager.getImageUrl());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }catch (Exception e){}

                    }else {
                        startActivity(new Intent(getApplicationContext(),Ac_Manager_Login.class));
                    }

                }else if (item.getItemId()==R.id.btnDrawerLogout){
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(com.raredevz.eventivo.Ac_Manager_Panel.this, "You'r logged out!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                        finish();
                    }

                }
                return true;
            }
        });
        //endregion

        firebaseAuth= FirebaseAuth.getInstance();
        fabAddVenue=findViewById(R.id.fabAddVenue);
        ry_my_venues=findViewById(R.id.ry_my_venues);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_ry_venue));
        ry_my_venues.addItemDecoration(dividerItemDecoration);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ry_my_venues.setLayoutManager(layoutManager);


        fabAddVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Ac_AddVanue.class));
            }
        });

        Dref= FirebaseDatabase.getInstance().getReference();
        Dref.child("Manager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (firebaseAuth.getCurrentUser()!=null){
                        manager=snapshot.child(firebaseAuth.getUid()).getValue(Manager.class);
                        if (manager!=null){
                            btnAccount.setVisibility(View.GONE);
                            btnAccount.setText(manager.getName());
                            btnAccount.setVisibility(View.VISIBLE);
                            btnAccount.setClickable(false);
                        }
                    }
                }catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Dref.child(Cons.node_venue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                venueList =new ArrayList<>();
                for (DataSnapshot city:snapshot.getChildren()){
                    for (DataSnapshot v:city.getChildren()){
                        Venue venue=v.getValue(Venue.class);
                        venue.setId(v.getKey());
                        if (venue.getManagerId().equals(FirebaseAuth.getInstance().getUid()))
                            venueList.add(venue);

                    }
                }
                VenueAdapter_Vertical adapter=new VenueAdapter_Vertical(com.raredevz.eventivo.Ac_Manager_Panel.this, venueList);
                adapter.setClickListener(com.raredevz.eventivo.Ac_Manager_Panel.this);
                ry_my_venues.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(View view, Venue venue) {
        Intent i=new Intent(getApplicationContext(), Ac_EditVenue.class);
        i.putExtra("venue",venue);
        startActivity(i);
    }
}