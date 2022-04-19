package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noor.mystore99.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class signUpPage extends AppCompatActivity {

    TextView textView;
    EditText name,phone,otp;
    Button sndOtp,submit;
    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListner authStateListner;
    FirebaseDatabase ref;
    DatabaseReference users;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String code;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    Animation topAnim,bottomAnim,midd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        textView=findViewById(R.id.textView12);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.LoginPhone);
        otp=findViewById(R.id.otpText);
        sndOtp=findViewById(R.id.OtpButton);
        submit=findViewById(R.id.submit);
        auth=FirebaseAuth.getInstance();
        ref=FirebaseDatabase.getInstance();
        users=ref.getReference("User");
        bottomAnim= AnimationUtils.loadAnimation(this, R.anim.downtoup);
        topAnim= AnimationUtils.loadAnimation(this, R.anim.uptodown);
        midd= AnimationUtils.loadAnimation(this, R.anim.myanimation);
        textView.setAnimation(topAnim);
        phone.setAnimation(bottomAnim);
        name.setAnimation(bottomAnim);
        sndOtp.setAnimation(bottomAnim);
        otp.setAnimation(bottomAnim);
        submit.setAnimation(bottomAnim);

        if(checkInternet()) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = preferences.edit();

            sndOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                                sendVerificationCode(phone);
                                //resendVerificationCode(editEmail,mResendToken)
                    sndOtp.setBackgroundResource(R.drawable.background5);
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(phone.getText().toString())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Phone Number", Snackbar.LENGTH_SHORT).show();

                    }

                    if (TextUtils.isEmpty(name.getText().toString())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Name", Snackbar.LENGTH_SHORT).show();

                    }
                    if (TextUtils.isEmpty(otp.getText().toString())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter OTP", Snackbar.LENGTH_SHORT).show();

                    }
                    else {
                        verifySignInCode(otp.getText().toString(), name, phone);
                    }

                }
            });


        }
        else{
            Toast.makeText(signUpPage.this,"No Connection",Toast.LENGTH_LONG).show();
        }


        }


    public void verifySignInCode(String codeEdit,EditText name,EditText phone){
        String newCode=codeEdit;
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, newCode);
        signInWithPhoneAuthCredential(credential,name,phone);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final EditText name, final EditText phone) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User obj=new User(name.getText().toString(),phone.getText().toString());
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
                            sendEmail(name.getText().toString(),phone.getText().toString());

                            Toast.makeText(signUpPage.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                           // SmsManager smsManager = SmsManager.getDefault();
                            //smsManager.sendTextMessage(phone.getText().toString(), null, String.valueOf(number)+" is your verification code for Sabzi Taza", null, null);
                            //Toast.makeText(getApplicationContext(), "SMS Sent!",
                              //      Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(signUpPage.this,"Incorrect code", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public void sendVerificationCode(EditText editPhone){
        String phoneNumber=editPhone.getText().toString();


        if(phoneNumber.isEmpty()){
            editPhone.setError("Phone Number is Required");
            editPhone.requestFocus();
            return;
        }
        if(phoneNumber.length()<10){
            editPhone.setError("Enter valid phone number");
            editPhone.requestFocus();
            return;
        }
        phoneNumber="+91"+phoneNumber;
        //String phone="+"+cpp.selectCoun


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // Toast.makeText(first.this,"hiiii",Toast.LENGTH_SHORT).show();

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            name=findViewById(R.id.name);
            phone=findViewById(R.id.LoginPhone);


            // In case OTP is received
            if (code != null) {
                otp.setText(code);
                verifySignInCode(code,name,phone);
            } else {
                // In case OTP is not received
                signInWithPhoneAuthCredential(phoneAuthCredential,name,phone);
            }

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mResendToken=forceResendingToken;
            code=s;
            Toast.makeText(signUpPage.this,"OTP send successfully!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    public boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;

    }
    private void sendEmail( String name, String phone) {
        //Getting content for email


        String str="Name "+name+"\n"+"Phone: "+phone;
//Creating SendMail object
        SendMail sm = new SendMail(this, "sabzitaza90@gmail.com", "New Registration", str);

        //Executing sendmail to send email
        sm.execute();
    }
}
