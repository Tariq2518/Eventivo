package com.raredevz.eventivo.Account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Ac_Manager_Panel;
import com.raredevz.eventivo.Admin.Ac_AdminHome;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.R;
import com.raredevz.eventivo.User.Ac_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ac_Manager_Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    EditText txtEmail,txtPassword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        firebaseAuth= FirebaseAuth.getInstance();
        
        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        btnSignIn=findViewById(R.id.btnSignIn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setTitle("Please wait!");
        
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getText(txtEmail) )){
                    AlertMessage.showMessage(Ac_Manager_Login.this,"Email is Required!");
                }else if (TextUtils.isEmpty(getText(txtPassword))){
                    AlertMessage.showMessage(Ac_Manager_Login.this,"Password is Required!");
                }else{
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(getText(txtEmail),getText(txtPassword))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    if (task.isSuccessful()){
                                        handleLogin();
//                                        if (firebaseAuth.getCurrentUser().isEmailVerified()){
//                                           handleLogin();
//                                        }else {
//                                            AlertMessage.showMessage(Ac_Manager_Login.this,"Please verify your email first!");
//                                        }

                                    }else {
                                        progressDialog.dismiss();
                                        firebaseAuth.signOut();
                                        AlertMessage.showMessage(Ac_Manager_Login.this,"Error "+task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });
    }

    private void handleLogin(){
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            if (firebaseAuth.getUid().equals("HD0wUmq0m1gMj9zJ0pVuzLdpG5O2")){
                startActivity(new Intent(getApplicationContext(), Ac_AdminHome.class));
                finish();
                return;
            }
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Manager").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressDialog.dismiss();
                    if (snapshot.hasChild(firebaseAuth.getUid())){
                        Manager manager=snapshot.child(firebaseAuth.getUid()).getValue(Manager.class);
                        if (manager.isApproved()){
                            startActivity(new Intent(getApplicationContext(), com.raredevz.eventivo.Ac_Manager_Panel.class));
                            finish();
                        }else {
                            AlertMessage.showMessage(Ac_Manager_Login.this,"You're not yet approved by the admin, please wait until admin approve your account.");
                        }

                    }else {
                        firebaseAuth.signOut();
                        AlertMessage.showMessage(Ac_Manager_Login.this,"You can't use User credentials to login as Manager!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    startActivity(new Intent(Ac_Manager_Login.this, Ac_Home.class));
                    Ac_Manager_Login.this.finish();

                }
            });
        }else {
            startActivity(new Intent(Ac_Manager_Login.this, Ac_Home.class));
            Ac_Manager_Login.this.finish();
        }
    }


    String getText(EditText txt){
        return txt.getText().toString();
    }
    public void signUp(View view) {
        Cons.otp_destination=Cons.otp_destination_admin;
        startActivity(new Intent(Ac_Manager_Login.this,VerifyNumber.class));
    }

    public void forgetPassword(View view) {
        EditText txtEmail=new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Forget Password?")
                .setView(txtEmail)
                .setMessage("Enter email to send password reset link.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(getText(txtEmail))){
                            AlertMessage.showMessage(Ac_Manager_Login.this,"Email is required");
                        }else {
                            firebaseAuth.sendPasswordResetEmail(getText(txtEmail)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        AlertMessage.showMessage(Ac_Manager_Login.this,"Email sent Successfully!");
                                    }else {
                                        AlertMessage.showMessage(Ac_Manager_Login.this,task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}