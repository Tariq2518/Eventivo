package com.raredevz.eventivo.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.Ac_Splash;
import com.raredevz.eventivo.Account.Ac_Login;
import com.raredevz.eventivo.Account.Ac_Manager_Login;
import com.raredevz.eventivo.Account.VerifyNumber;
import com.raredevz.eventivo.Chat.Constants;
import com.raredevz.eventivo.Chat.SettingsAPI;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.User;
import com.raredevz.eventivo.Helper.UserStatus;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.Helper.VenueAdapter_Horizontal;
import com.raredevz.eventivo.ML.ServiceGenerator;
import com.raredevz.eventivo.ML.Services;
import com.raredevz.eventivo.ML.compare_vanues;
import com.raredevz.eventivo.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ac_Home extends AppCompatActivity implements VenueAdapter_Horizontal.ItemClickListener {

    NavigationView navigationView;

    ArrayList<String> punjabCities;
    Spinner spCities;

    FirebaseAuth firebaseAuth;

    RecyclerView ry_suggested_venues, ry_nearby_venues;


    DatabaseReference Dref;

    List<Venue> suggestedvenueList, nearbyVenueList,allVenues;
    DrawerLayout drawer;
    EditText txtSearch;
    VenueAdapter_Horizontal adapter;

    LocationManager mLocationManager;
    Location mLocation;
    Button btnAccount;

    VenueAdapter_Horizontal nearByadapter;

    ArrayList<String> suggestedVenuesFromMl=new ArrayList<>();

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            mLocation=location;
            sortNearBy();
        }
        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    private void sortNearBy(){
        try {
            if (nearbyVenueList!=null){
                if (mLocation!=null){
//                    Collections.sort(nearbyVenueList, new Comparator<Venue>() {
//                        @Override
//                        public int compare(Venue o1, Venue o2) {
//                            return Float.compare((float)mLocation.distanceTo(o1.getLocation().toGoogleLocation()),(float)mLocation.distanceTo(o2.getLocation().toGoogleLocation()));
//                        }
//                    });
                 //   Toast.makeText(this, "Ordered!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //region Drawer_Navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtSearch = findViewById(R.id.txtSearch);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);

        }
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000,
                    10000, mLocationListener);
        }catch (Exception e){}


        drawer = (DrawerLayout) findViewById(R.id.drawer_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView=findViewById(R.id.nav_view);

        firebaseAuth= FirebaseAuth.getInstance();

        View view =navigationView.getHeaderView(0);
        btnAccount=view.findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cons.otp_destination=Cons.otp_destination_user;
                startActivity(new Intent(getApplicationContext(), Ac_Login.class));
            }
        });

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mfirebaseAuth) {
                if (mfirebaseAuth.getCurrentUser()!=null){
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.btnDrawerAddVenue).setVisible(false);
                    nav_Menu.findItem(R.id.btnDrawerLogout).setVisible(true);
                }else {
                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                    Menu nav_Menu = navigationView.getMenu();
                    nav_Menu.findItem(R.id.btnDrawerLogout).setVisible(false);
                    nav_Menu.findItem(R.id.btnDrawerAddVenue).setVisible(true);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.btnDrawerHome){
                    drawer.closeDrawer(GravityCompat.START);

                }
                if (item.getItemId()==R.id.btnDrawerSearchVenue){
                    drawer.closeDrawer(GravityCompat.START);
                    txtSearch.requestFocus();
                }
                if (item.getItemId()==R.id.btnDrawerAddVenue){
                    Cons.otp_destination=Cons.otp_destination_admin;
                    startActivity(new Intent(getApplicationContext(), Ac_Manager_Login.class));
                }else if (item.getItemId()==R.id.btnDrawerTermsAndCondition){
                    startActivity(new Intent(getApplicationContext(),Ac_TermsAndCondition.class));
                }else if (item.getItemId()==R.id.btnDrawerAboutUs){
                    startActivity(new Intent(getApplicationContext(),Ac_AboutUs.class));
                }else if (item.getItemId()==R.id.btnDrawerCompare){
                    startActivity(new Intent(getApplicationContext(), compare_vanues.class));
                }else if (item.getItemId()==R.id.btnDrawerMyBooking){
                    if (firebaseAuth.getCurrentUser()!=null){
                        startActivity(new Intent(getApplicationContext(),Ac_MyBookings.class));
                    }else{
                        Cons.otp_destination=Cons.otp_destination_user;
                        startActivity(new Intent(getApplicationContext(), Ac_Login.class));
                    }

                }else if (item.getItemId()==R.id.btnDrawerLogout){
                    if (firebaseAuth.getCurrentUser()!=null){
                        firebaseAuth.signOut();
                        Toast.makeText(Ac_Home.this, "You're logged out!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                    }else {
                        Cons.otp_destination=Cons.otp_destination_user;
                        startActivity(new Intent(getApplicationContext(), VerifyNumber.class));
                    }
                }
                return true;
            }
        });
        //endregion

        Dref= FirebaseDatabase.getInstance().getReference();
        Dref.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (firebaseAuth.getCurrentUser()!=null){
                        User manager=snapshot.child(firebaseAuth.getUid()).getValue(User.class);
                        //Toast.makeText(Ac_Home.this, ""+manager.getName(), Toast.LENGTH_SHORT).show();

                       // if (manager!=null){
                           // btnAccount.setVisibility(View.GONE);
                            //Toast.makeText(Ac_Home.this, ""+manager.getName(), Toast.LENGTH_SHORT).show();
                            btnAccount.setText(manager.getName());
                            btnAccount.setVisibility(View.VISIBLE);
                            btnAccount.setClickable(false);


                            SettingsAPI set;
                            set = new SettingsAPI(Ac_Home.this);
                            set.addUpdateSettings(Constants.PREF_MY_ID, FirebaseAuth.getInstance().getUid());
                            set.addUpdateSettings(Constants.PREF_MY_NAME, manager.getName());
                            set.addUpdateSettings(Constants.PREF_MY_DP, manager.getImageUrl());
                       // }

                       // Toast.makeText(Ac_Home.this, ""+manager.getStatus(), Toast.LENGTH_SHORT).show();

                        if (manager.getStatus().equals(UserStatus.Disabled)){
                            AlertMessage.showMessage(Ac_Home.this,"Your account has been disabled by the admin!");
                            try {
                                firebaseAuth.signOut();
                                startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                                finish();
                            }catch (Exception e){}
                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        s = ServiceGenerator.createService(Services.class);

        //region PunjabCities
        punjabCities=new ArrayList<>();
        punjabCities.add("Faisalabad");
        punjabCities.add("Ahmadpur East" );
        punjabCities.add("Ahmed Nager Chatha" );
        punjabCities.add("Ali Khan Abad" );
        punjabCities.add("Alipur" );
        punjabCities.add("Arifwala" );
        punjabCities.add("Attock" );
        punjabCities.add("Bhera" );
        punjabCities.add("Bhalwal" );
        punjabCities.add("Bahawalnagar"   );
        punjabCities.add("Bahawalpur"   );
        punjabCities.add("Bhakkar"   );
        punjabCities.add("Burewala"   );
        punjabCities.add("Chillianwala"   );
        punjabCities.add("Chakwal"   );
        punjabCities.add("Chichawatni"   );
        punjabCities.add("Chiniot"   );
        punjabCities.add("Chishtian"   );
        punjabCities.add("Daska"   );
        punjabCities.add("Darya Khan"   );
        punjabCities.add("Dera Ghazi Khan"   );
        punjabCities.add("Dhaular"   );
        punjabCities.add("Dina"   );
        punjabCities.add("Dinga"   );
        punjabCities.add("Dipalpur"   );
        punjabCities.add("Fateh Jang"   );
        punjabCities.add("Ghakhar Mandi"   );
        punjabCities.add("Gojra"   );
        punjabCities.add("Gujranwala"   );
        punjabCities.add("Gujrat"   );
        punjabCities.add("Gujar Khan"   );
        punjabCities.add("Hafizabad"   );
        punjabCities.add("Haroonabad"   );
        punjabCities.add("Hasilpur"   );
        punjabCities.add("Haveli Lakha"   );
        punjabCities.add("Jalalpur Jattan"   );
        punjabCities.add("Jampur"   );
        punjabCities.add("Jaranwala"   );
        punjabCities.add("Jhang"   );
        punjabCities.add("Jhelum"   );
        punjabCities.add("Kalabagh"   );
        punjabCities.add("Karor Lal Esan"   );
        punjabCities.add("Kasur"   );
        punjabCities.add("Kamalia"   );
        punjabCities.add("Kāmoke"   );
        punjabCities.add("Khanewal"   );
        punjabCities.add("Khanpur"   );
        punjabCities.add("Kharian"   );
        punjabCities.add("Khushab"   );
        punjabCities.add("Kot Adu"   );
        punjabCities.add("Jauharabad"   );
        punjabCities.add("Lahore"   );
        punjabCities.add("Lalamusa"   );
        punjabCities.add("Layyah"   );
        punjabCities.add("Liaquat Pur"   );
        punjabCities.add("Lodhran"   );
        punjabCities.add("Malakwal"   );
        punjabCities.add("Mamoori"   );
        punjabCities.add("Mailsi"   );
        punjabCities.add("Mandi Bahauddin"   );
        punjabCities.add("Mian Channu"   );
        punjabCities.add("Mianwali"   );
        punjabCities.add("Multan"   );
        punjabCities.add("Murree"   );
        punjabCities.add("Muridke"   );
        punjabCities.add("Mianwali Bangla"   );
        punjabCities.add("Muzaffargarh"   );
        punjabCities.add("Narowal"   );
        punjabCities.add("Okara"   );
        punjabCities.add("Renala Khurd"   );
        punjabCities.add("Pakpattan"   );
        punjabCities.add("Pattoki"   );
        punjabCities.add("Pir Mahal"   );
        punjabCities.add("Qaimpur"   );
        punjabCities.add("Qila Didar Singh"   );
        punjabCities.add("Rabwah"   );
        punjabCities.add("Raiwind"   );
        punjabCities.add("Rajanpur"   );
        punjabCities.add("Rahim Yar Khan"   );
        punjabCities.add("Rawalpindi"   );
        punjabCities.add("Sadiqabad"   );
        punjabCities.add("Safdarabad"   );
        punjabCities.add("Sahiwal"   );
        punjabCities.add("Sangla Hill"   );
        punjabCities.add("Sarai Alamgir"   );
        punjabCities.add("Sargodha"   );
        punjabCities.add("Shakargarh"   );
        punjabCities.add("Sheikhupura"   );
        punjabCities.add("Sialkot"   );
        punjabCities.add("Sohawa"   );
        punjabCities.add("Soianwala"   );
        punjabCities.add("Siranwali"   );
        punjabCities.add("Talagang"   );
        punjabCities.add("Taxila"   );
        punjabCities.add("Toba Tek Singh"   );
        punjabCities.add("Vehari"   );
        punjabCities.add("Wah Cantonment"   );
        punjabCities.add("Wazirabad"   );

        //endregion


        spCities=findViewById(R.id.spCities);
        ry_suggested_venues=findViewById(R.id.ry_suggested_venues);
        ry_nearby_venues=findViewById(R.id.ry_nearby_venues);

        ArrayAdapter cadapter=new ArrayAdapter(Ac_Home.this,R.layout.li_sp_simple,punjabCities);
        spCities.setAdapter(cadapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager nlayoutManager = new LinearLayoutManager(this);
        nlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        ry_suggested_venues.setLayoutManager(layoutManager);

        ry_nearby_venues.setLayoutManager(nlayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider_ry_venue));
        ry_suggested_venues.addItemDecoration(dividerItemDecoration);
        ry_nearby_venues.addItemDecoration(dividerItemDecoration);




        suggestedvenueList =new ArrayList<>();
        adapter=new VenueAdapter_Horizontal(Ac_Home.this, suggestedvenueList);

        Dref.child(Cons.node_venue).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                suggestedvenueList =new ArrayList<>();
                nearbyVenueList=new ArrayList<>();
                allVenues=new ArrayList<>();
                for (DataSnapshot city:snapshot.getChildren()){
                    for (DataSnapshot v:city.getChildren()){
                        Venue venue=v.getValue(Venue.class);
                        venue.setId(v.getKey());
                        suggestedvenueList.add(venue);
                        allVenues.add(venue);
                        if (city.getKey().equals(spCities.getSelectedItem().toString())){
                            if (nearbyVenueList.size()<7)
                                nearbyVenueList.add(venue);
                        }
                    }
                }
                Collections.shuffle(nearbyVenueList);
                sortNearBy();
                 adapter=new VenueAdapter_Horizontal(Ac_Home.this, suggestedvenueList);
                predict_Service(1,0,0);
                nearByadapter=new VenueAdapter_Horizontal(Ac_Home.this, nearbyVenueList);
                ry_suggested_venues.setAdapter(adapter);
                ry_nearby_venues.setAdapter(nearByadapter);

                spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        List<Venue> venues=allVenues;
                        nearbyVenueList=new ArrayList<>();
                        for (int i=0;i<7;i++){
                            Venue venue=venues.get(i);


                                if (punjabCities.get(position).equalsIgnoreCase(venue.getCity())){
                                    nearbyVenueList.add(venue);
                                }


                        }
                        Collections.shuffle(nearbyVenueList);
                        nearByadapter=new VenueAdapter_Horizontal(Ac_Home.this, nearbyVenueList);
                        nearByadapter.notifyDataSetChanged();
                        ry_nearby_venues.setAdapter(nearByadapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                adapter.setClickListener(Ac_Home.this);
                nearByadapter.setClickListener(Ac_Home.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    @Override
    public void onItemClick(View view, Venue venue) {

        Intent i=new Intent(Ac_Home.this,Ac_Venue_Details.class);
        i.putExtra("venue",venue);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
       //startActivity(new Intent(getApplicationContext(), Ac_Home.class));
       //finish();
    }

    Services s;
    public void predict_Service(final int attribute_1, final int attribute_2, final int attribute_3)
    {

        // Toast.makeText(this, "Service Called", Toast.LENGTH_SHORT).show();
        String json = "{\n" +
                "\n" +
                "        \"Inputs\": {\n" +
                "\n" +
                "                \"input1\":\n" +
                "                {\n" +
                "                    \"ColumnNames\": [\"Objective\", \"res_1\", \"res_2\"],\n" +
                "                    \"Values\": [ [ \"" + attribute_1 + "\" , \"" + attribute_2 + "\", \"" + attribute_3 + "\"], [ \"" + attribute_1 + "\", \"" + attribute_2 + "\", \"" + attribute_3 + "\"] ]\n" +
                "                }       },\n" +
                "            \"GlobalParameters\": {\n" +
                "}\n" +
                "    }";
//        Toast.makeText(this, "" + json, Toast.LENGTH_SHORT).show();
        JsonObject obj = new JsonObject();
        try
        {

            JsonParser jsonParser = new JsonParser();
            obj = (JsonObject)jsonParser.parse(json);


            Log.d("My App", obj.toString());

        }
        catch (Throwable t)
        {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
        s.getNotification(obj,
                "Bearer 9KMnA02M97yKRl7JSIN7ihk/sXRw4RkPIi2DfAKjFnqT/SI3n7wZoYv1OTJRsY0ofmGIzqXXp7dZvMTlIoPGew==")
                .enqueue(new Callback<ResponseBody>()
                {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        String b = "";
                        try
                        {
                            b = new String(response.body()
                                    .bytes());
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                      //  Toast.makeText(Ac_Home.this, b, Toast.LENGTH_SHORT)
                       //         .show();
                        b = b.toString();
                        String[] recomended_venue =  b.split("Values", 2);
                        String recomended = recomended_venue[1];
                        recomended = recomended.replace("\"]]}}}}","");
                        recomended = recomended.replace("\":[[\"","");

                        String[] vanues_according_to_hirarichy = recomended.split(",");

                        String mesaage_to_show = "";
                        suggestedVenuesFromMl=new ArrayList<>();
                        for(String vanue : vanues_according_to_hirarichy)
                        {
                            suggestedVenuesFromMl.add(vanue);
                            mesaage_to_show = mesaage_to_show + vanue +"\n";
                        }


                        if (suggestedvenueList!=null){
                            if (suggestedvenueList.size()>0){
                                ArrayList<Venue> allVenues=new ArrayList<>();
                                allVenues.addAll(suggestedvenueList);
                                suggestedvenueList=new ArrayList<>();
                                for (Venue venue:allVenues){
                                    String Name=venue.getName();
                                    Name=Name.replaceAll(" ","");
                                    if (suggestedVenuesFromMl.contains(Name)){
                                        suggestedvenueList.add(venue);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();


                       // rec.setText(mesaage_to_show);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {

                    }
                });
    }
}