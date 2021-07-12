package com.raredevz.eventivo.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Ac_Splash;
import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;

public class Ac_AdminHome extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void ManagerRequest(View view) {
        startActivity(new Intent(getApplicationContext(),Ac_ManagerRequests.class));
    }

    public void userFeedback(View view) {
        startActivity(new Intent(getApplicationContext(),Ac_Venue_Feedback.class));
    }

    public void ManageUser(View view) {
        startActivity(new Intent(getApplicationContext(),Ac_User_accounts.class));
    }

    public void logout(View view) {
        try {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Ac_Splash.class));
            finish();
        }catch (Exception e){
            startActivity(new Intent(getApplicationContext(), Ac_Splash.class));
            finish();
        }
    }
}