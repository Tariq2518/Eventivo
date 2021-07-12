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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Admin.Ac_AdminHome;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.R;
import com.raredevz.eventivo.User.Ac_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ac_Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    EditText txtEmail,txtPassword;
    Button btnSignIn;
    TextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();

        txtEmail=findViewById(R.id.txtEmail);
        txtPassword=findViewById(R.id.txtPassword);
        btnSignIn=findViewById(R.id.btnSignIn);
        tvForgetPassword=findViewById(R.id.tvForgetPassword);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setTitle("Please wait!");

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword(v);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getText(txtEmail))){
                    Toast.makeText(Ac_Login.this, "Email is Required!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(getText(txtPassword))){
                    Toast.makeText(Ac_Login.this, "Password is Required!", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(getText(txtEmail),getText(txtPassword))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        FirebaseUser user=firebaseAuth.getCurrentUser();
                                        handleLogin();
                                    }else {
                                        AlertMessage.showMessage(Ac_Login.this,"Error "+task.getException().getMessage());
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
            if (firebaseAuth.getUid().equals("yhecpWJHgJaFGAlkekhsB9RGNH33")){
                startActivity(new Intent(getApplicationContext(), Ac_AdminHome.class));
                finish();
                return;
            }
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Manager").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(firebaseAuth.getUid())){
                        firebaseAuth.signOut();
                       AlertMessage.showMessage(Ac_Login.this,"You can't use Manger credentials to login as User!");

                    }else {
                        startActivity(new Intent(getApplicationContext(),Ac_Home.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    startActivity(new Intent(Ac_Login.this, Ac_Home.class));
                    Ac_Login.this.finish();

                }
            });
        }else {
            startActivity(new Intent(Ac_Login.this, Ac_Home.class));
            Ac_Login.this.finish();
        }
    }

    String getText(EditText txt){
        return txt.getText().toString();
    }
    public void signUp(View view) {
        Cons.otp_destination=Cons.otp_destination_user;
        startActivity(new Intent(Ac_Login.this,VerifyNumber.class));
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
                            AlertMessage.showMessage(Ac_Login.this,"Email is required");
                        }else {
                            firebaseAuth.sendPasswordResetEmail(getText(txtEmail)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        AlertMessage.showMessage(Ac_Login.this,"Email sent Successfully!");
                                    }else {
                                        AlertMessage.showMessage(Ac_Login.this,task.getException().getMessage());
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