package com.raredevz.eventivo.Manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Location;
import com.raredevz.eventivo.Helper.Venue;
import com.raredevz.eventivo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ac_AddVanue extends AppCompatActivity {


    CircleImageView imgProfile;

    private static final int READ_REQUEST_CODE = 42;
    public Uri filePath;
    String DownloadImageUrl;

    ProgressDialog progressDialog;
    EditText txtName,txtLocation,txtCapacity,txtCharges,txtAcctNum;

    ArrayList<String> punjabCities;
    Spinner spCities;

    Button btnAddVenue;

    Boolean dj=false,decoration=false;
    String type;

    RadioButton rbMarriageHall,rbPartyHall,rbMarquee;
    CheckBox cbDj,cbDecoration;

    Venue venue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vanue);
        
        txtName=findViewById(R.id.txtVenueName);
        txtLocation=findViewById(R.id.txtLocation);
        txtCapacity=findViewById(R.id.txtCapacity);
        txtCharges=findViewById(R.id.txtCharges);
        txtAcctNum=findViewById(R.id.txtAcctNum);

        imgProfile=findViewById(R.id.imgAddVenueImage);

        btnAddVenue=findViewById(R.id.btnAddVenue);

        rbMarriageHall=findViewById(R.id.rbMarriageHall);
        rbPartyHall=findViewById(R.id.rbPartyHall);
        rbMarquee=findViewById(R.id.rbMarquee);

        cbDj=findViewById(R.id.cbDJ);
        cbDecoration=findViewById(R.id.cbDecoration);



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
        ArrayAdapter cadapter=new ArrayAdapter(this,R.layout.li_sp_simple,punjabCities);
        spCities.setAdapter(cadapter);



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setTitle("Please wait!");

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        rbPartyHall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    type= Cons.venueType_PartyHall;
                }
            }
        });

        rbMarriageHall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    type=Cons.venueType_MarriageHall;
                }
            }
        });

        rbMarquee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    type=Cons.venueType_Marquee;
            }
        });


        cbDj.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dj=isChecked;
            }
        });

        cbDecoration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                decoration=isChecked;
            }
        });
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(31.4504, 73.1350)  // Initial Latitude and Longitude the Map will load into
                        .showLatLong(true)  // Show Coordinates in the Activity
                        .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
                        .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                        .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
                        //.setMarkerDrawable(R.drawable.marker) // Change the default Marker Image
                        .setMarkerImageImageColor(R.color.colorPrimary)
                        .setFabColor(R.color.colorPrimary)
                        .setPrimaryTextColor(android.R.color.white) // Change text color of Shortened Address
                        .setSecondaryTextColor(android.R.color.darker_gray) // Change text color of full Address
                        // .setBottomViewColor(R.color.bottomViewColor) // Change Address View Background Color (Default: White)
                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
                        .setMapType(MapType.NORMAL)
                        //.setPlaceSearchBar(true, GOOGLE_API_KEY) //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
                        .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                        .build(Ac_AddVanue.this);

                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
            }
        });

        btnAddVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath==null){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Select an image!");

                }else if (TextUtils.isEmpty(txtName.getText().toString())){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Name is required!");
                    txtName.requestFocus();
                }else if (TextUtils.isEmpty(txtLocation.getText()) || location==null){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Select venue location");
                }else if (TextUtils.isEmpty(txtCapacity.getText().toString())){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Enter a capacity value!");
                    txtCapacity.requestFocus();
                }else if (TextUtils.isEmpty(txtCharges.getText().toString())){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Enter a Charges value!");
                    txtCharges.requestFocus();
                }else if (TextUtils.isEmpty(txtAcctNum.getText().toString())){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Enter a Account number to receive payments");
                    txtAcctNum.requestFocus();
                }else if(type==null){
                    AlertMessage.showMessage(Ac_AddVanue.this,"Select a venue type!");
                }else {
                    venue=new Venue();
                    try {
                        venue.setCapacity(Integer.parseInt(txtCapacity.getText().toString()));
                    }catch (Exception e){
                        AlertMessage.showMessage(Ac_AddVanue.this,"Enter valid sitting capacity!\\ni.e. 100,200...");

                        txtCapacity.requestFocus();
                    }
                    try {
                        venue.setCharges(Integer.parseInt(txtCharges.getText().toString()));
                    }catch (Exception e){
                        AlertMessage.showMessage(Ac_AddVanue.this,"Enter valid Charges!\\ni.e. 30000,40000...");

                        txtCharges.requestFocus();
                    }

                    venue.setName(txtName.getText().toString());
                    venue.setLocation(location);
                    venue.setDj(dj);
                    venue.setDecoration(decoration);
                    venue.setType(type);
                    venue.setAcctNumber(txtAcctNum.getText().toString());
                    venue.setCity(spCities.getSelectedItem().toString());
                    venue.setManagerId(FirebaseAuth.getInstance().getUid());

                    uploadImage();

                }
            }
        });
        
    }
    Location location;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                location=new Location();
                location.setLatitude(addressData.getLatitude());
                location.setLongitude(addressData.getLongitude());
                txtLocation.setText(addressData.toString());
            }
        }
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                //  Toast.makeText(this, "Uri : " + uri.toString(), Toast.LENGTH_SHORT).show();
                ShowImage(uri);
            }
        }
    }

    //region Selecting_Image_&_Uploading_Image
    public void SelectImage() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void ShowImage(Uri uri){
        Bitmap image=null;
        try {
            image=getBitmapFromUri(uri);
            imgProfile.setImageBitmap(image);
        } catch (IOException e) {
            // Toast.makeText(this, "Exception While Reading Image : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        filePath=uri;
        return image;
    }

    StorageReference storageReference;
    private void uploadImage() {
        storageReference= FirebaseStorage.getInstance().getReference();
        if(filePath != null) {

            try {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                final StorageReference ref = storageReference.child("venue_images/" + UUID.randomUUID().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadUrl;
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                downloadUrl = urlTask.getResult();
                                DownloadImageUrl = downloadUrl.toString();
                                venue.setImageUrl(DownloadImageUrl);
                                DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
                                dref.child("Venues").child(venue.getCity()).push().setValue(venue)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()){
                                                    Toast.makeText(Ac_AddVanue.this, "Venue Added", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }else {
                                                    Toast.makeText(Ac_AddVanue.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                              
                                //   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Ac_AddVanue.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Ac_AddVanue.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No Image is Selected", Toast.LENGTH_SHORT).show();
        }

    }
    private void showDialog(){
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setMessage("Please do contact with Mr. Syed Ahmad Sherazi having contact 0321-7903172 for FEE submission and to view contents!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    }).show();
        }catch (WindowManager.BadTokenException e){

        }

    }
    //endregion
}