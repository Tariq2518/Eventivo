package com.raredevz.eventivo.Account;

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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Ac_Splash;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Location;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.UserStatus;
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
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ac_ManagerSignUp extends AppCompatActivity {

    EditText txtName,txtContact,txtEmail,txtPassword;

    Button btnSignUp;

    CircleImageView imgProfile;

    private static final int READ_REQUEST_CODE = 42;
    public Uri filePath;
    String DownloadImageUrl;

    ProgressDialog progressDialog;
    Location location;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_sign_up);

        txtName=findViewById(R.id.txtName);
        txtContact=findViewById(R.id.txtContact);
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);

        btnSignUp=findViewById(R.id.btnSignUp);

        imgProfile=findViewById(R.id.imgProfile);

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



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getText(txtName))){
                    AlertMessage.showMessage(Ac_ManagerSignUp.this,"Name is Required!");
                }else  if (TextUtils.isEmpty(getText(txtContact))){
                    AlertMessage.showMessage(Ac_ManagerSignUp.this,"Contact is Required");

                }else if (TextUtils.isEmpty(getText(txtEmail)) && Patterns.EMAIL_ADDRESS.matcher(getText(txtEmail)).matches()){
                    AlertMessage.showMessage(Ac_ManagerSignUp.this,"Enter valid EMail address!");

                }else if (filePath==null){
                    AlertMessage.showMessage(Ac_ManagerSignUp.this,"Select Profile Image");
                }else{
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(getText(txtEmail),getText(txtPassword))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()){
                                        firebaseUser=firebaseAuth.getCurrentUser();
                                        uploadImage();
//                                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//
//
//                                                }else {
//                                                    firebaseUser.delete();
//                                                    AlertMessage.showMessage(Ac_ManagerSignUp.this,"Error "+task.getException().getMessage());
//                                                }
//                                            }
//                                        });
                                    }else {
                                        AlertMessage.showMessage(Ac_ManagerSignUp.this,"Error "+task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });

    }
    FirebaseUser firebaseUser;

    String getText(EditText txt){
        return txt.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
//                final ProgressDialog progressDialog = new ProgressDialog(this);
//                progressDialog.setTitle("Uploading...");
//                progressDialog.show();

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
                                Manager manager=new Manager();
                                manager.setName(getText(txtName));
                                manager.setContact(getText(txtContact));
                                manager.setImageUrl(DownloadImageUrl);
                                manager.setId(firebaseAuth.getUid());
                                manager.setEmail(txtEmail.getText().toString());
                                manager.setApproved(false);
                                manager.setStatus(UserStatus.Enabled);
                                DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
                                dref.child("Manager").child(firebaseAuth.getUid()).setValue(manager)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if (task.isSuccessful()){
                                                    try {
                                                        firebaseAuth.signOut();
                                                    }catch (Exception e){}
                                                    new AlertDialog.Builder(Ac_ManagerSignUp.this)
                                                            .setMessage("Your manager account has been created successfully, you can login after the approval of admin.")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Splash.class));
                                                                    finish();
                                                                }
                                                            }).show();
                                                }else {
                                                    Toast.makeText(Ac_ManagerSignUp.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                //   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                firebaseUser.delete();
                                progressDialog.dismiss();
                                Toast.makeText(Ac_ManagerSignUp.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(Ac_ManagerSignUp.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), Ac_Manager_Login.class));
                            finish();
                        }
                    }
                });
            }catch (Exception e){
                firebaseUser.delete();
                Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No Image is Selected", Toast.LENGTH_SHORT).show();
        }

    }
    //endregion
}
