package com.raredevz.eventivo.ML;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;

import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class compare_vanues extends AppCompatActivity {

    TextView mTextView;
    Services s;
    TextView bes;
    SearchableSpinner tvSelect1,tvSelect2;
    ArrayList<String> venuesList;
    ArrayList<String> venueNamesList;
    List<Venue> allVenuesList;
    ArrayAdapter<String> adapterSelect1,adapterSelect2;

    TextView tvTasteOfFood1,tvQualityOfFood1,tvMenuVariety1,tvValueForTheMoney1,
            tvOrderAccuracy1,tvAmbiance1,tvOveralSatisfaction1,tvSpeedOfService1,
     tvTasteOfFood2,tvQualityOfFood2,tvMenuVariety2,tvValueForTheMoney2,tvOrderAccuracy2
            ,tvAmbiance2,tvOveralSatisfaction2,tvSpeedOfService2;

    ContentLoadingProgressBar progressBar;

    private FusedLocationProviderClient fusedLocationClient;
    Location mLocation;
    Venue venue1,venue2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_vanues);

        mTextView = (TextView) findViewById(R.id.text);
        s = ServiceGenerator.createService(Services.class);
        bes = (TextView) findViewById(R.id.compare);

        tvTasteOfFood1=findViewById(R.id.tasteOfFood1);
        tvQualityOfFood1=findViewById(R.id.QoF1);
        tvMenuVariety1=findViewById(R.id.MV1);
        tvValueForTheMoney1=findViewById(R.id.VFM1);
        tvOrderAccuracy1=findViewById(R.id.OA1);
        tvAmbiance1=findViewById(R.id.Amb1);
        tvOveralSatisfaction1=findViewById(R.id.OAS1);
        tvSpeedOfService1=findViewById(R.id.SOS1);

        tvTasteOfFood2=findViewById(R.id.tasteOfFood2);
        tvQualityOfFood2=findViewById(R.id.QoF2);
        tvMenuVariety2=findViewById(R.id.MV2);
        tvValueForTheMoney2=findViewById(R.id.VFM2);
        tvOrderAccuracy2=findViewById(R.id.OA2);
        tvAmbiance2=findViewById(R.id.Amb2);
        tvOveralSatisfaction2=findViewById(R.id.OAS2);
        tvSpeedOfService2=findViewById(R.id.SOS2);


        progressBar=findViewById(R.id.progressBar);


        venueNamesList=new ArrayList<>();
        venueNamesList.add("BandhanMarriageHall");
        venueNamesList.add("KoheToorMarquee");
        venueNamesList.add("VictoriaMarquee");
        venueNamesList.add("ParadiseGrandMarquee");
        venueNamesList.add("PearlMarriageHall");
        venueNamesList.add("QuilimMarquee");
        venueNamesList.add("LaraibBanquet");
        venueNamesList.add("FaisalabadMarriageHall");
        venueNamesList.add("CrystalsBanquetHall");
        venueNamesList.add("SevenSeasMarquee");
        venueNamesList.add("TheCastleMarquee");
        venueNamesList.add("TheSheratonMarquee");
        venueNamesList.add("LayalpurMarquee");
        venueNamesList.add("StarMarquee");
        venueNamesList.add("KhayyamBanquetHall");
        venueNamesList.add("TamaseelMarriageHall");
        venueNamesList.add("RoyalPalaceMarquee");
        venueNamesList.add("RoyalPalmMarquee");
        venueNamesList.add("GulistanMarriageHall");
        venueNamesList.add("WeddingMarquee");
        venueNamesList.add("TheRoyalBanquet");
        venueNamesList.add("CrownPalaceMarquee");
        venueNamesList.add("JumeirahPalace");
        //Replace with any two venus according to the input
        /*reserse_dic_map = {0: 'BandhanMarriageHall',1: 'KoheToorMarquee',2: 'VictoriaMarquee',
                3:'ParadiseGrandMarquee', 4:'PearlMarriageHall',5: 'QuilimMarquee',
                6:'LaraibBanquet',7: 'FaisalabadMarriageHall', 8:'CrystalsBanquetHall',
                9:'SevenSeasMarquee',10: 'TheCastleMarquee',11: 'TheSheratonMarquee',
                12:'LayalpurMarquee',13: 'StarMarquee',14: 'KhayyamBanquetHall',15: 'TamaseelMarriageHall',
                16:'RoyalPalaceMarquee',17: 'RoyalPalmMarquee',18: 'GulistanMarriageHall',19: 'WeddingMarquee',
                20:'TheRoyalBanquet',21: 'CrownPalaceMarquee',22: 'JumeirahPalace'}

            Here 1 means BandhanMarriageHall
            2 means KoheToorMarquee and so on
        */
        int vanue_1 = 1;
        int vanue_2 = 3;
      //  predict_Service(0,vanue_1,vanue_2);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            mLocation=location;
                            if (location != null) {
                                if (venue1!=null){
                                    tvOrderAccuracy1.setText(String.format("%.3f",String.valueOf(location.distanceTo(venue1.getLocation().toGoogleLocation()) / 1000)) + " KM");
                                }
                                if (venue2!=null){
                                    tvOrderAccuracy2.setText(String.format("%.3f",String.valueOf(location.distanceTo(venue2.getLocation().toGoogleLocation()) / 1000)) + " KM");
                                }

                                //Toast.makeText(Ac_Venue_Details.this, "Location" + location.toString(), Toast.LENGTH_SHORT).show();
                                //tvVenueDistance.setText();
                                //   Toast.makeText(Ac_Venue_Details.this, "Distance " + (location.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM", Toast.LENGTH_SHORT).show();

                                // Logic to handle location object
                            }
                        }
                    });
        }


        tvSelect1=findViewById(R.id.tvSelect1);
        tvSelect2=findViewById(R.id.tvSelect2);
        DatabaseReference Dref= FirebaseDatabase.getInstance().getReference();
        Dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                venuesList =new ArrayList<>();
                allVenuesList=new ArrayList<>();
                for (DataSnapshot city:snapshot.child(Cons.node_venue).getChildren()){
                    for (DataSnapshot v:city.getChildren()){
                        Venue venue=v.getValue(Venue.class);
                        venue.setId(v.getKey());
                        allVenuesList.add(venue);

                    }
                }
                for (String s:venueNamesList){
                    venuesList.add(s.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2"));
                }
                adapterSelect1=new ArrayAdapter<>(compare_vanues.this, R.layout.li_searchable_dropdown_white,venuesList);
                adapterSelect2=new ArrayAdapter<>(compare_vanues.this,R.layout.li_searchable_dropdown_white ,venuesList);

                tvSelect1.setTitle("Select Venue 1");
                tvSelect2.setTitle("Select Venue 2");

                tvSelect1.setAdapter(adapterSelect1);
                tvSelect2.setAdapter(adapterSelect2);

                tvSelect1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Venue venue=null;//=allVenuesList.get(position);
                       // Toast.makeText(compare_vanues.this, venuesList.get(position), Toast.LENGTH_SHORT).show();
                        for (Venue venues:allVenuesList){
                            if (venues.getName().equalsIgnoreCase(venuesList.get(position))){
                                venue=venues;
                                Toast.makeText(compare_vanues.this, venue.getName(), Toast.LENGTH_SHORT).show();
                            }
                            for (String name:venuesList){


                            }
                        }
                        tvTasteOfFood1.setText(venue.getType());
                        tvQualityOfFood1.setText(String.valueOf(venue.getCapacity()));
                        tvMenuVariety1.setText(String.valueOf(venue.getCharges()/venue.getCapacity()));
                        tvValueForTheMoney1.setText(venue.getCity());
                        if (mLocation!=null){
                            tvOrderAccuracy1.setText(String.valueOf(mLocation.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM");
                        }
                        StringBuilder stringBuilder=new StringBuilder();
                        if (venue.isDj() && venue.isDecoration())
                            stringBuilder.append("Dj,\nDecoration");
                        else {
                            if (venue.isDj())
                                stringBuilder.append("DJ");
                            if (venue.isDecoration())
                                stringBuilder.append("Decoration");
                        }


                        tvAmbiance1.setText(stringBuilder.toString());

                        tvOveralSatisfaction1.setText(String.format("%.2f",venue.getRating())+"/5");

//                        for (DataSnapshot d:snapshot.child("Feedback").child(venue.getId()).getChildren()){
//                            tvTasteOfFood1.setText(d.child("TasteofFood").getValue(String.class));
//                            tvQualityOfFood1.setText(d.child("QualityofFood").getValue(String.class));
//                            tvMenuVariety1.setText(d.child("ManuVariety").getValue(String.class));
//                            tvValueForTheMoney1.setText(d.child("ValuefortheMoney").getValue(String.class));
//                            tvOrderAccuracy1.setText(d.child("OrderAccuracy").getValue(String.class));
//                            tvAmbiance1.setText(d.child("Ambiance").getValue(String.class));
//                            tvOveralSatisfaction1.setText(d.child("OverallSatisfaction").getValue(String.class));
//                            tvSpeedOfService1.setText(d.child("SpeedofService").getValue(String.class));
//
//                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                tvSelect2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Venue venue=null;//=allVenuesList.get(position);
                        // Toast.makeText(compare_vanues.this, venuesList.get(position), Toast.LENGTH_SHORT).show();
                        for (Venue venues:allVenuesList){
                            if (venues.getName().equalsIgnoreCase(venuesList.get(position))){
                                venue=venues;
                                Toast.makeText(compare_vanues.this, venue.getName(), Toast.LENGTH_SHORT).show();
                            }
                            for (String name:venuesList){


                            }
                        }
                        tvTasteOfFood2.setText(venue.getType());
                        tvQualityOfFood2.setText(String.valueOf(venue.getCapacity()));
                        tvMenuVariety2.setText(String.valueOf(venue.getCharges()/venue.getCapacity()));
                        tvValueForTheMoney2.setText(venue.getCity());



                        StringBuilder stringBuilder=new StringBuilder();
                        if (venue.isDj() && venue.isDecoration())
                            stringBuilder.append("Dj,\nDecoration");
                        else {
                            if (venue.isDj())
                                stringBuilder.append("DJ");
                            if (venue.isDecoration())
                                stringBuilder.append("Decoration");
                        }


                        tvAmbiance2.setText(stringBuilder.toString());
                        tvOveralSatisfaction2.setText(String.format("%.2f",venue.getRating())+"/5");
                        if (mLocation!=null){
                            tvOrderAccuracy2.setText(String.valueOf(mLocation.distanceTo(venue.getLocation().toGoogleLocation()) / 1000) + " KM");
                        }
//                        for (DataSnapshot d:snapshot.child("Feedback").child(venue.getId()).getChildren()){
//                            tvTasteOfFood2.setText(d.child("TasteofFood").getValue(String.class));
//                            tvQualityOfFood2.setText(d.child("QualityofFood").getValue(String.class));
//                            tvMenuVariety2.setText(d.child("ManuVariety").getValue(String.class));
//                            tvValueForTheMoney2.setText(d.child("ValuefortheMoney").getValue(String.class));
//                            tvOrderAccuracy2.setText(d.child("OrderAccuracy").getValue(String.class));
//                            tvAmbiance2.setText(d.child("Ambiance").getValue(String.class));
//                            tvOveralSatisfaction2.setText(d.child("OverallSatisfaction").getValue(String.class));
//                            tvSpeedOfService2.setText(d.child("SpeedofService").getValue(String.class));
//
//                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void predict_Service(final int attribute_1, final int attribute_2, final int attribute_3)
    {

        progressBar.setVisibility(View.VISIBLE);
        bes.setVisibility(View.GONE);
        bes.setText("");
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
        System.out.println(json);
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

//
//                        Toast.makeText(compare_vanues.this, b, Toast.LENGTH_SHORT)
//                                .show();
//
//                        Toast.makeText(compare_vanues.this, b, Toast.LENGTH_SHORT)
//                                .show();
//                        b = b.toString();
                        String[] best_venue =  b.split("Values", 2);
                        String best = best_venue[1];
                        best = best.replace("\"]]}}}}","");
                        best = best.replace("\":[[\"","");
//                        Toast.makeText(compare_vanues.this, "Name is : "+best, Toast.LENGTH_SHORT)
//                                .show();
                        best=best.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
                        progressBar.setVisibility(View.GONE);
                        bes.setVisibility(View.VISIBLE);
                        bes.setText(best);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {

                    }
                });
    }

    public void CompareWithML(View view) {
        int vanue_1 =tvSelect1.getSelectedItemPosition();//venueNamesList.indexOf(tvSelect1.getSelectedItem().toString().replace(" ",""))+1;
        int vanue_2 = tvSelect2.getSelectedItemPosition();//venueNamesList.indexOf(tvSelect2.getSelectedItem().toString().replace(" ",""))+1;;
          predict_Service(0,vanue_1,vanue_2);

    }
}