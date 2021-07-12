package com.raredevz.eventivo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.raredevz.eventivo.Admin.Ac_AdminHome;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.UserStatus;
import com.raredevz.eventivo.User.Ac_Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ac_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Animation animation= AnimationUtils.loadAnimation(this,R.anim.bottom_up);


        ImageView imageView=findViewById(R.id.imgLogoSlogan);
        imageView.startAnimation(animation);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return;

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        new CountDownTimer(1500,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

               // startActivity(new Intent(getApplicationContext(), Ac_AdminHome.class));
               handleLogin();
            }
        }.start();
    }

    private void handleLogin(){

        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()!=null){
            if (firebaseAuth.getCurrentUser().getUid().equals("tEcgDWpbZRTEmguK9fzvcmaFa7q1")){
                startActivity(new Intent(getApplicationContext(), Ac_AdminHome.class));
                finish();
            }else {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Manager").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(firebaseAuth.getUid())){
                            Manager manager=snapshot.child(firebaseAuth.getUid()).getValue(Manager.class);
                            //Toast.makeText(Ac_Splash.this, firebaseAuth.getUid(), Toast.LENGTH_LONG).show();
                            if (manager.isApproved()){
                                if (manager.getStatus().equals(UserStatus.Enabled)){
                                    startActivity(new Intent(getApplicationContext(), Ac_Manager_Panel.class));
                                    finish();
                                }else {
                                 AlertMessage.showMessage(com.raredevz.eventivo.Ac_Splash.this,"Your account has been disabled by the admin!");
                                }
                            }else {
                                AlertMessage.showMessage(com.raredevz.eventivo.Ac_Splash.this,"You're not yet approved by the admin, please wait until admin approve your account.");
                            }
                        }else {
                            startActivity(new Intent(getApplicationContext(), Ac_Home.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        startActivity(new Intent(com.raredevz.eventivo.Ac_Splash.this, Ac_Home.class));
                        com.raredevz.eventivo.Ac_Splash.this.finish();

                    }
                });
            }

        }else {
            startActivity(new Intent(com.raredevz.eventivo.Ac_Splash.this, Ac_Home.class));
            com.raredevz.eventivo.Ac_Splash.this.finish();
        }
    }
}