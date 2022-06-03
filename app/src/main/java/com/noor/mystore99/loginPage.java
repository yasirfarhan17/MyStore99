package com.noor.mystore99;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

import dmax.dialog.SpotsDialog;

public class loginPage extends AppCompatActivity {
    TextView text;
    EditText phone, otp;
    Button sndOtp, submit;
    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListner authStateListner;
    FirebaseDatabase ref;
    DatabaseReference users;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    SharedPreferences preferences1;
    SharedPreferences.Editor editor1;
    String code;
    Animation topAnim, bottomAnim, midd;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final int SEND_SMS_CODE = 23;
    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        text = findViewById(R.id.textView11);
        phone = findViewById(R.id.LoginPhone);
        otp = findViewById(R.id.optText);
        sndOtp = findViewById(R.id.OtpButton);

        submit = findViewById(R.id.submit);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance();
        users = ref.getReference("User");
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        midd = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        text.setAnimation(topAnim);
        phone.setAnimation(bottomAnim);
        sndOtp.setAnimation(bottomAnim);
        otp.setAnimation(bottomAnim);
        submit.setAnimation(bottomAnim);


        if (checkInternet()) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = preferences.edit();
            preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
            editor1 = preferences1.edit();


            sndOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    users.orderByChild("phone").equalTo(phone.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                sendVerificationCode(phone);
                                //resendVerificationCode(editEmail,mResendToken);
                            } else {
                                Toast.makeText(loginPage.this, "Please Register Fist!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    sndOtp.setBackgroundResource(R.drawable.background5);
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(phone.getText().toString())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Phone Number", Snackbar.LENGTH_SHORT).show();
                        return;

                    }

                    if (TextUtils.isEmpty(otp.getText().toString())) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter OTP", Snackbar.LENGTH_LONG).show();
                        return;

                    }
                    if (number != 0) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "working", Snackbar.LENGTH_SHORT).show();
                        final android.app.AlertDialog waitingtDialog = new SpotsDialog.Builder().setContext(loginPage.this).build();
                        waitingtDialog.show();
                        users = FirebaseDatabase.getInstance().getReference("User");
                        if (number == Integer.parseInt(otp.getText().toString())) {
                            verifySignInCode(String.valueOf(number), otp.getText().toString());

                        }
                        waitingtDialog.dismiss();

                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "working22", Snackbar.LENGTH_SHORT).show();
                        final android.app.AlertDialog waitingtDialog = new SpotsDialog.Builder().setContext(loginPage.this).build();
                        waitingtDialog.show();
                        users = FirebaseDatabase.getInstance().getReference("User");
                        verifySignInCode(otp.getText().toString());
                        waitingtDialog.dismiss();
                    }
                }
            });


        } else {
            Toast.makeText(loginPage.this, "No Connection", Toast.LENGTH_LONG).show();
        }


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();


                // In case OTP is received
                if (code != null) {
                    otp.setText(code);
                    verifySignInCode(code);
                } else {
                    // In case OTP is not received
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mResendToken = forceResendingToken;
                code = s;
                Toast.makeText(loginPage.this, "OTP send successfully!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try {
                    SmsManager smsManager = SmsManager.getDefault();

                    Random rnd = new Random();
                    int number = rnd.nextInt(999999);
                    smsManager.sendTextMessage(phone.getText().toString(), null, String.valueOf(number) + " is your verification code for Sabzi Taza", null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(loginPage.this, "Access Denied ! Cannot proceed further ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
/*
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
*/
    }

    public void verifySignInCode(String codeEdit) {

        try {
            String newCode = codeEdit;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, newCode);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    public void verifySignInCode(String codeEdit, String val) {

        try {
            String newCode = codeEdit;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(val, newCode);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(loginPage.this, MainActivity.class);
                            editor.putString("key", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                            editor.apply();
                            startActivity(intent);

                        } else {
                            Toast.makeText(loginPage.this, "Incorrect code", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public void sendVerificationCode(EditText editPhone) {
        String phoneNumber = editPhone.getText().toString();


        if (phoneNumber.isEmpty()) {
            editPhone.setError("Phone Number is Required");
            editPhone.requestFocus();
            return;
        }
        if (phoneNumber.length() < 10) {
            editPhone.setError("Enter valid phone number");
            editPhone.requestFocus();
            return;
        }
        phoneNumber = "+91" + phoneNumber;
        //String phone="+"+cpp.selectCoun


/*
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
*/
        // Toast.makeText(first.this,"hiiii",Toast.LENGTH_SHORT).show();

    }


    public boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;

    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
