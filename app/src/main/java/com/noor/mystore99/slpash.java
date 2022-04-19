package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class slpash extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;
    FirebaseAuth auth;
    TextView text;
    CircleImageView img;
    Animation topAnim,bottomAnim,midd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);
        auth=FirebaseAuth.getInstance();
        bottomAnim= AnimationUtils.loadAnimation(this, R.anim.downtoup);
        topAnim= AnimationUtils.loadAnimation(this, R.anim.uptodown);
        midd= AnimationUtils.loadAnimation(this, R.anim.myanimation);
        text=findViewById(R.id.slpashText);
        text.setAnimation(topAnim);

    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(slpash.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },SPLASH_SCREEN);
            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(slpash.this,page1.class);
                        startActivity(intent);
                        finish();
                    }
                },SPLASH_SCREEN);
            }
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }
}
