package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderPage extends AppCompatActivity {
    DatabaseReference ref;
    SharedPreferences preferences,preferences1;
    SharedPreferences.Editor editor ;
    Button back;

    String key,val,ad;
    final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    final String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    ArrayList<String> arr=new ArrayList<>();
    RecyclerView recyclerView1;
    MyOrderAdapter adapter;
    int p=0;
    ArrayList<Long> time=new ArrayList<>();


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("key",key);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences1.edit();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        back=findViewById(R.id.back);


        recyclerView1=findViewById(R.id.cart_recyclerView1);
        //t3=findViewById(R.id.totalPrice);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("Item").child("YourOrder");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    String val=dt.getKey().toString();
                    arr.add(val);


                }
                Collections.reverse(arr);
                adapter=new MyOrderAdapter(arr,OrderPage.this);
                adapter.notifyDataSetChanged();
                recyclerView1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
