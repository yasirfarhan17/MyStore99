package com.noor.mystore99;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class page1 extends AppCompatActivity {
    Button signUp,Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        signUp=findViewById(R.id.signUp);
        Login=findViewById(R.id.submit);

        Login.setOnClickListener(view -> {
            Intent intent=new Intent(page1.this,MainActivity.class);
            startActivity(intent);
            Login.setBackgroundResource(R.drawable.background5);
        });

        signUp.setOnClickListener(view -> {
            Intent intent=new Intent(page1.this,signUpPage.class);
            startActivity(intent);
            signUp.setBackgroundResource(R.drawable.background5);
        });
    }
}
