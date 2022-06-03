package com.noor.mystore99;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;
public class first extends AppCompatActivity {

    Button btnsignIn,btnRegister,LoginVerify;
    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListner authStateListner;
    FirebaseDatabase ref;
    DatabaseReference users;
    RelativeLayout rootLayout;
    String code,email,pass;
   // private CountryCodePicker cpp;
    User user;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


  /*  FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                Intent intent = new Intent(first.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        if(checkInternet()){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            editor = preferences.edit();
            // session = new SessionManager(getApplicationContext());

            rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);
            auth=FirebaseAuth.getInstance();
            ref=FirebaseDatabase.getInstance();
            users=ref.getReference("User");

            btnRegister=(Button)findViewById(R.id.btnRegister);
            btnsignIn=(Button)findViewById(R.id.btnSignIn);
            // session.checkLogin();
       /* HashMap<String, String> user=session.getUserDetail();
         email=user.get(session.EMAIL);
         pass=user.get(session.PASSWORD);*/

            //Toast.makeText(first.this,email+" "+pass,Toast.LENGTH_SHORT).show();
        /* if(email !=null && pass !=null){
             Intent intent = new Intent(first.this, MainActivity.class);
             //editor.putString("key",val);
             //editor.apply();
             startActivity(intent);
             finish();
         }*/


            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRegisterDilog();
                }
            });

            btnsignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLoginDilog();
                }
            });

        }

        else{
            Toast.makeText(first.this,"No Connection",Toast.LENGTH_LONG).show();
        }


    }






    private void showLoginDilog() {
        final AlertDialog.Builder dilog=new AlertDialog.Builder(this);
        dilog.setTitle("SIGN IN");
        dilog.setMessage("Please use phone number to sign in");

        LayoutInflater inflater= LayoutInflater.from(first.this);
        View login_layout=inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText editEmail=login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText editPassword=login_layout.findViewById(R.id.LoginCode);
        LoginVerify=login_layout.findViewById(R.id.LoginVerify);
        editEmail.setText(email);
        editPassword.setText(pass);


        dilog.setView(login_layout);

        LoginVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.orderByChild("phone").equalTo(editEmail.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            sendVerificationCode(editEmail);
                            //resendVerificationCode(editEmail,mResendToken);
                        }
                        else{
                            Toast.makeText(first.this,"Please Register Fist!!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            });


        dilog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();



                if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Phone Number", Snackbar.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter OTP", Snackbar.LENGTH_SHORT).show();
                    return;

                }
                final android.app.AlertDialog waitingtDialog = new SpotsDialog.Builder().setContext(first.this).build();
                waitingtDialog.show();
                users = FirebaseDatabase.getInstance().getReference("User");
                verifySignInCode(editPassword.getText().toString());
                waitingtDialog.dismiss();


               /* users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Toast.makeText(first.this,editEmail.getText().toString(),Toast.LENGTH_LONG).show();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final String val = dataSnapshot1.getKey();
                            //Toast.makeText(first.this,editEmail.getText().toString()+""+editPassword.getText().toString(),Toast.LENGTH_SHORT).show();

                            users.child(val).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String phone = dataSnapshot.child("phone").getValue(String.class);
                                        String pass = dataSnapshot.child("password").getValue(String.class);

                                        if (phone.equals(editEmail.getText().toString()) && pass.equals(editPassword.getText().toString())) {
                                            //Toast.makeText(first.this,""+FirebaseAuth.getInstance().getCurrentUser(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(first.this, MainActivity.class);
                                            intent.putExtra("phone", editEmail.getText().toString());
                                            intent.putExtra("key", val);
                                            editor.putString("key",val);
                                            editor.apply();
                                            session.createSession(editEmail.getText().toString(),editPassword.getText().toString());
                                            startActivity(intent);
                                            waitingtDialog.dismiss();

                                        }

                                    } else {
                                        Toast.makeText(first.this, "Phone number and password not match", Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            waitingtDialog.dismiss();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dilog.show();
    }

    private void showRegisterDilog() {
        final AlertDialog.Builder dilog=new AlertDialog.Builder(this);
        dilog.setTitle("REGISTER");
        dilog.setMessage("use phone number");

        LayoutInflater inflater= LayoutInflater.from(this);
        View register_layout=inflater.inflate(R.layout.layout_register,null);

        //final MaterialEditText editEmail=register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText editName=register_layout.findViewById(R.id.edtName);
        final MaterialEditText editPhone=register_layout.findViewById(R.id.edtPhone);
        final Button Butn=register_layout.findViewById(R.id.btn);
        final MaterialEditText editcode=register_layout.findViewById(R.id.edtcode);
        // cpp=register_layout.findViewById(R.id.cpp);

        dilog.setView(register_layout);

        Butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(editPhone);
            }
        });


        dilog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (TextUtils.isEmpty(editPhone.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Phone Number", Snackbar.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(editName.getText().toString())) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Name", Snackbar.LENGTH_SHORT).show();

                }

                    verifySignInCode(editcode,editName,editPhone);


            }
        });


        dilog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dilog.show();
    }

    public void verifySignInCode(MaterialEditText codeEdit,MaterialEditText name,MaterialEditText phone){
        String newCode=codeEdit.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, newCode);
        signInWithPhoneAuthCredential(credential,name,phone);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final MaterialEditText name, final MaterialEditText phone) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           User obj=new User(name.getText().toString(),phone.getText().toString());
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
                            sendEmail(name.getText().toString(),phone.getText().toString());

                            Toast.makeText(first.this,"Registration Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(first.this,"Incorrect code", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    public void verifySignInCode(String codeEdit){

        try {
            String newCode=codeEdit;
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code, newCode);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(first.this,MainActivity.class);
                            editor.putString("key", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                            editor.apply();
                            startActivity(intent);

                        } else {
                            Toast.makeText(first.this,"Incorrect code", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void resendVerificationCode(MaterialEditText editPhone,
                                        PhoneAuthProvider.ForceResendingToken token) {
        Toast.makeText(first.this,""+token,Toast.LENGTH_SHORT).show();
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
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks


    }

    public void sendVerificationCode(MaterialEditText editPhone){
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


/*
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutor.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
*/
       // Toast.makeText(first.this,"hiiii",Toast.LENGTH_SHORT).show();

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


            // In case OTP is received
            if (code != null) {
               // edit.setText(code);
                verifySignInCode(code);
            } else {
                // In case OTP is not received
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mResendToken=forceResendingToken;
            code=s;
            Toast.makeText(first.this,"OTP send successfully!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };

    private void sendEmail( String name, String phone) {
        //Getting content for email


        String str="Name "+name+"\n"+"Phone: "+phone;
//Creating SendMail object
        SendMail sm = new SendMail(this, "sabzitaza90@gmail.com", "New Registration", str);

        //Executing sendmail to send email
        sm.execute();
    }

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
}
