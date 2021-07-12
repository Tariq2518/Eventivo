package com.raredevz.eventivo.Account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Ac_Splash;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Location;
import com.raredevz.eventivo.Helper.User;
import com.raredevz.eventivo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ac_UserSignup extends AppCompatActivity {

    ArrayList<String> punjabCities;
    ArrayList<String> sindhCities;
    ArrayList<String> kpkCities;
    ArrayList<String> balochistanCities;
    ArrayList<String> ajkCities;
    ArrayList<String> gilgitCities;

    Spinner spProvince,spCities;

    CircleImageView imgProfile;

    EditText txtName,txtContact,txtEmail,txtAddress,txtPassword,txtLocation;

    Button btnSignUp;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    private static final int READ_REQUEST_CODE = 42;
    public Uri filePath;
    String DownloadImageUrl;

    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        spProvince=findViewById(R.id.spProvince);
        spCities=findViewById(R.id.spCities);

        imgProfile=findViewById(R.id.imgProfile);

        txtName=findViewById(R.id.txtName);
        txtContact=findViewById(R.id.txtContact);
        txtAddress=findViewById(R.id.txtAddress);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        txtLocation=findViewById(R.id.txtLocation);

        btnSignUp=findViewById(R.id.btnSignUp);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setTitle("Please wait!");

        firebaseAuth= FirebaseAuth.getInstance();
        
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


        //region PunjabCities
        punjabCities=new ArrayList<>();
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
        punjabCities.add("Faisalabad"   );
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

        //region sindhCities
        sindhCities=new ArrayList<>();
        sindhCities.add("Badin");
        sindhCities.add("Bhirkan");
        sindhCities.add("Rajo Khanani");
        sindhCities.add("Chak");
        sindhCities.add("Dadu");
        sindhCities.add("Digri");
        sindhCities.add("Diplo");
        sindhCities.add("Dokri");
        sindhCities.add("Ghotki");
        sindhCities.add("Haala");
        sindhCities.add("Hyderabad");
        sindhCities.add("Islamkot");
        sindhCities.add("Jacobabad");
        sindhCities.add("Jamshoro");
        sindhCities.add("Jungshahi");
        sindhCities.add("Kandhkot");
        sindhCities.add("Kandiaro");
        sindhCities.add("Karachi");
        sindhCities.add("Kashmore");
        sindhCities.add("Keti Bandar");
        sindhCities.add("Khairpur");
        sindhCities.add("Kotri");
        sindhCities.add("Larkana");
        sindhCities.add("Matiari");
        sindhCities.add("Mehar");
        sindhCities.add("Mirpur Khas");
        sindhCities.add("Mithani");
        sindhCities.add("Mithi");
        sindhCities.add("Mehrabpur");
        sindhCities.add("Moro");
        sindhCities.add("Nagarparkar");
        sindhCities.add("Naudero");
        sindhCities.add("Naushahro Feroze");
        sindhCities.add("Naushara");
        sindhCities.add("Nawabshah");
        sindhCities.add("Nazimabad");
        sindhCities.add("Qambar");
        sindhCities.add("Qasimabad");
        sindhCities.add("Ranipur");
        sindhCities.add("Ratodero");
        sindhCities.add("Rohri");
        sindhCities.add("Sakrand");
        sindhCities.add("Sanghar");
        sindhCities.add("Shahbandar");
        sindhCities.add("Shahdadkot");
        sindhCities.add("Shahdadpur");
        sindhCities.add("Shahpur Chakar");
        sindhCities.add("Shikarpaur");
        sindhCities.add("Sukkur");
        sindhCities.add("Tangwani");
        sindhCities.add("Tando Adam Khan");
        sindhCities.add("Tando Allahyar");
        sindhCities.add("Tando Muhammad Khan");
        sindhCities.add("Thatta");
        sindhCities.add("Umerkot");
        sindhCities.add("Warah");

        //endregion

        //region kpkCities
        kpkCities=new ArrayList<>();
        kpkCities.add("Abbottabad");
        kpkCities.add("Adezai");
        kpkCities.add("Alpuri");
        kpkCities.add("Akora Khattak");
        kpkCities.add("Ayubia");
        kpkCities.add("Banda Daud Shah");
        kpkCities.add("Bannu");
        kpkCities.add("Batkhela");
        kpkCities.add("Battagram");
        kpkCities.add("Birote");
        kpkCities.add("Chakdara");
        kpkCities.add("Charsadda");
        kpkCities.add("Chitral");
        kpkCities.add("Daggar");
        kpkCities.add("Dargai");
        kpkCities.add("Darya Khan");
        kpkCities.add("Dera Ismail Khan");
        kpkCities.add("Doaba");
        kpkCities.add("Dir");
        kpkCities.add("Drosh");
        kpkCities.add("Hangu");
        kpkCities.add("Haripur");
        kpkCities.add("Karak");
        kpkCities.add("Kohat");
        kpkCities.add("Kulachi");
        kpkCities.add("Lakki Marwat");
        kpkCities.add("Latamber");
        kpkCities.add("Madyan");
        kpkCities.add("Mansehra");
        kpkCities.add("Mardan");
        kpkCities.add("Mastuj");
        kpkCities.add("Mingora");
        kpkCities.add("Nowshera");
        kpkCities.add("Paharpur");
        kpkCities.add("Pabbi");
        kpkCities.add("Peshawar");
        kpkCities.add("Saidu Sharif");
        kpkCities.add("Shorkot");
        kpkCities.add("Shewa Adda");
        kpkCities.add("Swabi");
        kpkCities.add("Swat");
        kpkCities.add("Tangi");
        kpkCities.add("Tank");
        kpkCities.add("Thall");
        kpkCities.add("Timergara");
        kpkCities.add("Tordher");
        //endregion

        //region Balochistan-Cities
        balochistanCities=new ArrayList<>();
        balochistanCities.add("Bela");
        balochistanCities.add("Chaman");
        balochistanCities.add("Dadhar");
        balochistanCities.add("Dera Bugti");
        balochistanCities.add("Gwadar");
        balochistanCities.add("Hala");
        balochistanCities.add("Jiwani");
        balochistanCities.add("Kalat");
        balochistanCities.add("Kharan");
        balochistanCities.add("Kot Malik");
        balochistanCities.add("Loralai");
        balochistanCities.add("Mach");
        balochistanCities.add("Mastung");
        balochistanCities.add("Mehrabpur");
        balochistanCities.add("Nushki");
        balochistanCities.add("Pasni");
        balochistanCities.add("Pishin");
        balochistanCities.add("Quetta");
        balochistanCities.add("Sibi");
        balochistanCities.add("Turbat");
        balochistanCities.add("Usta Muhammad");
        balochistanCities.add("Uthal");
        balochistanCities.add("Zhob");
        //endregion

        //region AJ-K_Cities
        ajkCities=new ArrayList<>();
        ajkCities.add("Muzaffarabad");
        ajkCities.add("Neelum");
        ajkCities.add("Hattian");
        ajkCities.add("Mirpur");
        ajkCities.add("Haveli");
        ajkCities.add("Poonch");
        ajkCities.add("Rawalakot");
        ajkCities.add("Kel");
        ajkCities.add("Sharda");
        ajkCities.add("Bagh");
        ajkCities.add("Kotli");
        ajkCities.add("Keran");
        ajkCities.add("Sudhnuti");
        ajkCities.add("Bhimber");

        //endregion

        //region Gilgit_Cities
        gilgitCities=new ArrayList<>();
        gilgitCities.add("Aliabad");
        gilgitCities.add("Askole");
        gilgitCities.add("Astore");
        gilgitCities.add("Bunji");
        gilgitCities.add("Chilas");
        gilgitCities.add("Chillinji");
        gilgitCities.add("Cholt");
        gilgitCities.add("Dambudas");
        gilgitCities.add("Danyor");
        gilgitCities.add("Darel");
        gilgitCities.add("Dansam");
        gilgitCities.add("Eidghah");
        gilgitCities.add("Gahkuch");
        gilgitCities.add("Gilgit");
        gilgitCities.add("Gojal");
        gilgitCities.add("Gulmit");
        gilgitCities.add("Gultari");
        gilgitCities.add("Haldi");
        gilgitCities.add("Hopar");
        gilgitCities.add("Hussainabad");
        gilgitCities.add("Ishkoman");
        gilgitCities.add("Juglot");
        gilgitCities.add("Jalalabad");
        gilgitCities.add("Jutial");
        gilgitCities.add("Karimabad");
        gilgitCities.add("Khaplu");
        gilgitCities.add("Kharfaq");
        gilgitCities.add("Kharko");
        gilgitCities.add("Kharmang");
        gilgitCities.add("Mayoon");
        gilgitCities.add("Minimarg");
        gilgitCities.add("Misgar");
        gilgitCities.add("Nasirabad");
        gilgitCities.add("Nagar");
        gilgitCities.add("Oshikhandas");
        gilgitCities.add("Passu");
        gilgitCities.add("Punial");
        gilgitCities.add("Qasimabad");
        gilgitCities.add("Roundu");
        gilgitCities.add("Shigar");
        gilgitCities.add("Shimshal");
        gilgitCities.add("Skardu");
        gilgitCities.add("Sultanabad");
        gilgitCities.add("Sust");
        gilgitCities.add("Tangir");
        gilgitCities.add("Thole");
        gilgitCities.add("Thowar");
        gilgitCities.add("Tolti");
        gilgitCities.add("Yasin");

        //endregion


        ArrayList<String> provinceList=new ArrayList<>();
        provinceList.add("Province");
        provinceList.add("Punjab");
        provinceList.add("Sindh");
        provinceList.add("K.P.K");
        provinceList.add("Balochistan");
        provinceList.add("Azad Kashmir");
        provinceList.add("Gilgit-Baltistan");
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.li_sp,provinceList);

        spProvince.setAdapter(adapter);
        
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList d=new ArrayList();
                d.add("City");
                ArrayAdapter cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,d);;
                if (spProvince.getSelectedItem().toString().equals("Punjab")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,punjabCities);
                }else if (spProvince.getSelectedItem().toString().equals("Sindh")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,sindhCities);
                }else if (spProvince.getSelectedItem().toString().equals("K.P.K")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,kpkCities);
                }else if (spProvince.getSelectedItem().toString().equals("Balochistan")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,balochistanCities);
                }else if (spProvince.getSelectedItem().toString().equals("Azad Kashmir")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,ajkCities);
                }else if (spProvince.getSelectedItem().toString().equals("Gilgit-Baltistan")){
                    cadapter=new ArrayAdapter(Ac_UserSignup.this,R.layout.li_sp,gilgitCities);
                }

                spCities.setAdapter(cadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getText(txtName))){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Name is Required!");
                }else  if (TextUtils.isEmpty(getText(txtContact))){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Contact is Required!");
                }else if (TextUtils.isEmpty(getText(txtAddress))){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Address is Required!");
                }else if (spProvince.getSelectedItem().toString().equals("Province")){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Select Province");
                }else if (spCities.getSelectedItem().toString().equals("City")){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Select City");
                }else if (TextUtils.isEmpty(getText(txtEmail)) && Patterns.EMAIL_ADDRESS.matcher(getText(txtEmail)).matches()){
                    AlertMessage.showMessage(Ac_UserSignup.this,"Enter valid Email address!");
                }else{
                    progressDialog.show();
//                    if (filePath!=null){
//                        progressDialog.dismiss();
//                        uploadImage();
//                    }else{
//                        User user=new User();
//                        user.setName(getText(txtName));
//                        user.setContact(getText(txtContact));
//                        user.setId(firebaseAuth.getUid());
//                        user.setCity(spCities.getSelectedItem().toString());
//                        user.setProvince(spProvince.getSelectedItem().toString());
//                        user.setAddress(getText(txtAddress));
//                        user.setEmail(getText(txtEmail));
//                        user.setStatus(UserStatus.Enabled);
//                        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
//                        dref.child("User").child(firebaseAuth.getUid()).setValue(user)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        progressDialog.dismiss();
//                                        if (task.isSuccessful()){
//                                           // firebaseAuth.signOut();
//                                            Toast.makeText(Ac_UserSignup.this, "Sign up successful", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(), Ac_Splash.class));
//                                            finish();
//                                        }else {
//                                            Toast.makeText(Ac_UserSignup.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
                    firebaseAuth.createUserWithEmailAndPassword(getText(txtEmail),getText(txtPassword))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        final FirebaseUser fuser=firebaseAuth.getCurrentUser();
                                        assert fuser != null;
                                        fuser.sendEmailVerification().addOnCompleteListener(
                                                new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            if (filePath!=null){
                                                                progressDialog.dismiss();
                                                                uploadImage();
                                                            }else{
                                                                User user=new User();
                                                                user.setName(getText(txtName));
                                                                user.setContact(getText(txtContact));
                                                                user.setId(firebaseAuth.getUid());
                                                                user.setCity(spCities.getSelectedItem().toString());
                                                                user.setProvince(spProvince.getSelectedItem().toString());
                                                                user.setAddress(getText(txtAddress));
                                                                user.setEmail(getText(txtEmail));
                                                                DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
                                                                dref.child("User").child(firebaseAuth.getUid()).setValue(user)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                progressDialog.dismiss();
                                                                                if (task.isSuccessful()){
                                                                                    firebaseAuth.signOut();
                                                                                    Toast.makeText(Ac_UserSignup.this, "Sign up successful, Verify your email!", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                                                                                    finish();
                                                                                }else {

                                                                                    fuser.delete();
                                                                                    Toast.makeText(Ac_UserSignup.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }

                                                        }else {
                                                            progressDialog.dismiss();
                                                            AlertMessage.showMessage(Ac_UserSignup.this,"Error "+task.getException().getMessage());
                                                            fuser.delete();
                                                        }
                                                    }
                                                }
                                        );


                                    }else {
                                        progressDialog.dismiss();
                                        AlertMessage.showMessage(Ac_UserSignup.this,"Error "+task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });


    }
    String getText(EditText txt){
        return txt.getText().toString();
    }

    //region Selecting_Image_&_Uploading_Image
    public void SelectImage() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);



        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                //  Toast.makeText(this, "Uri : " + uri.toString(), Toast.LENGTH_SHORT).show();
                ShowImage(uri);
            }
        }
    }
    public void ShowImage(Uri uri){
        Bitmap image=null;
        try {
            image=getBitmapFromUri(uri);
            imgProfile.setImageBitmap(image);
        } catch (IOException e) {

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

                final StorageReference ref = storageReference.child("profile_images/" + UUID.randomUUID().toString());
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downloadUrl;
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                downloadUrl = urlTask.getResult();
                                DownloadImageUrl = downloadUrl.toString();
                                User user=new User();
                                user.setName(getText(txtName));
                                user.setContact(getText(txtContact));
                                user.setId(firebaseAuth.getUid());
                                user.setCity(spCities.getSelectedItem().toString());
                                user.setProvince(spProvince.getSelectedItem().toString());
                                user.setAddress(getText(txtAddress));
                                user.setEmail(getText(txtEmail));
                                user.setImageUrl(DownloadImageUrl);
                                DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
                                dref.child("User").child(firebaseAuth.getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()){
                                                   // firebaseAuth.signOut();
                                                    Toast.makeText(Ac_UserSignup.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                                                    finish();
                                                }else {
                                                    Toast.makeText(Ac_UserSignup.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Ac_UserSignup.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }catch (Exception e){
                Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No Image is Selected", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion
}