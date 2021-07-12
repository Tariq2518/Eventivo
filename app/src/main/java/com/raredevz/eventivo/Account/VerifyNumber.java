
package com.raredevz.eventivo.Account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.Ac_Manager_Panel;
import com.raredevz.eventivo.Admin.Ac_AdminHome;
import com.raredevz.eventivo.Helper.AlertMessage;
import com.raredevz.eventivo.Helper.Cons;
import com.raredevz.eventivo.Helper.Manager;
import com.raredevz.eventivo.Helper.User;
import com.raredevz.eventivo.Helper.UserStatus;
import com.raredevz.eventivo.R;
import com.raredevz.eventivo.User.Ac_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class VerifyNumber extends AppCompatActivity {
    private static final String TAG = "PhoneAuth";
    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;
    private Button signoutButton;
    private TextView statusText;
    CountryCodePicker ccp;
    EditText editTextCarrierNumber;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    String destinationActivity;

    private FirebaseAuth fbAuth;

    static boolean oldUser;
    DatabaseReference dref;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);

//        destinationActivity=getIntent().getExtras().getString("DestActivity");
        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        signoutButton = (Button) findViewById(R.id.signoutButton);
        statusText = (TextView) findViewById(R.id.statusText);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.phoneText);

        ccp.setDefaultCountryUsingNameCode("PK");
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);


        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);
        signoutButton.setEnabled(false);
        statusText.setText("Signed Out");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setMessage("Processing...");

        fbAuth = FirebaseAuth.getInstance();

        dref= FirebaseDatabase.getInstance().getReference();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendCode(v);
            }
        });
    }

    public void sendCode(View view) {

        String phoneNumber = ccp.getFullNumberWithPlus().toString();//phoneText.getText().toString();
        if (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Toast.makeText(this, "Please use \"+923001234567\" Format", Toast.LENGTH_SHORT).show();
            return;
        }

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
        progressDialog.show();
    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {


                        signoutButton.setEnabled(true);
                        statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        progressDialog.dismiss();

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Toast.makeText(VerifyNumber.this, "Invalid credential: "
                                    + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Toast.makeText(VerifyNumber.this, "SMS Quota exceeded.", Toast.LENGTH_SHORT).show();
                        }else {
                            showAlert(e.getMessage());
                            Toast.makeText(VerifyNumber.this, "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        progressDialog.dismiss();
                        phoneVerificationId = verificationId;
                        resendToken = token;
                        Toast.makeText(VerifyNumber.this, "Code Sent Successfully!", Toast.LENGTH_SHORT).show();
                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);
                    }
                };
    }

    public void verifyCode(View view) {

        String code = codeText.getText().toString();
        progressDialog.show();
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        try{
            fbAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signoutButton.setEnabled(true);
                                codeText.setText("");
                                statusText.setText("Signed In");
                                resendButton.setEnabled(false);
                                verifyButton.setEnabled(false);
                               handleLogin();
                            } else {
                                progressDialog.dismiss();
                                if (task.getException() instanceof
                                        FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                    Toast.makeText(VerifyNumber.this, "Invalid Code Entered "+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(VerifyNumber.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            showAlert(e.getMessage());
        }
    }

    private void handleLogin(){
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressDialog.dismiss();
                    if (snapshot.child("Manager").hasChild(firebaseUser.getUid())){
                        Manager manager=snapshot.child("Manager").child(firebaseUser.getUid()).getValue(Manager.class);
                        if (manager.isApproved()){
                            if (manager.getStatus().equals(UserStatus.Enabled)){
                                startActivity(new Intent(getApplicationContext(), Ac_Manager_Panel.class));
                                finish();
                            }else {
                                AlertMessage.showMessage(VerifyNumber.this,"Your account has been disabled by the admin!");
                            }
                        }else {
                            AlertMessage.showMessage(VerifyNumber.this,"You're not yet approved by the admin, please wait until admin approve your account.");
                        }
                    }else if (snapshot.child("User").hasChild(firebaseUser.getUid())){
                        User user=snapshot.child("User").child(firebaseUser.getUid()).getValue(User.class);
                        if (user!=null){
                           // if (user.getStatus().equals(UserStatus.Enabled)){
                                startActivity(new Intent(getApplicationContext(),Ac_Home.class));
                                finish();
//                            }else {
//                                AlertMessage.showMessage(VerifyNumber.this,"Your account has been disabled by the admin!");
//                            }
                        }else{
                            startActivity(new Intent(getApplicationContext(),Ac_UserSignup.class));
                        }

                    }else if (firebaseUser.getUid().equals("tEcgDWpbZRTEmguK9fzvcmaFa7q1")){
                        startActivity(new Intent(getApplicationContext(), Ac_AdminHome.class));
                        finish();
                    }else {
                        if (Cons.otp_destination.equals(Cons.otp_destination_admin)){
                            startActivity(new Intent(getApplicationContext(),Ac_ManagerSignUp.class));
                        }else {
                            startActivity(new Intent(getApplicationContext(),Ac_UserSignup.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void resendCode(View view) {

        String phoneNumber = ccp.getFullNumberWithPlus().toString();//phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }

    public void signOut(View view) {
        fbAuth.signOut();
        statusText.setText("Signed Out");
        signoutButton.setEnabled(false);
        sendButton.setEnabled(true);
    }
    
    private void showAlert(String msg){
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .show();
    }



}