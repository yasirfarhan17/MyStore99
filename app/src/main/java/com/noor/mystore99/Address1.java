package com.noor.mystore99;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Address1 extends AppCompatActivity {
    ArrayList<cartItem> product1=new ArrayList<>();

    TextView saveAdd,savePhone,total;
    EditText date,pin;
    Button  btn,pay,back,chngePin;
    DatabaseReference ref;
    SharedPreferences preferences;
    String key,val1,ad,num;
    int rate;
    SharedPreferences preferences1;
    SharedPreferences.Editor editor ;
    SharedPreferences preferences3;
    ArrayList<pinCode> pinList=new ArrayList<>();
    ArrayList<String> ppn=new ArrayList<>();
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    int hr,hr1;
    //SharedPreferences.Editor editor ;

    Handler handler=new Handler();

    private Runnable page=new Runnable() {
        @Override
        public void run() {
            if(saveAdd.getText().toString()==""||saveAdd.getText().toString().isEmpty()){
                pay.setEnabled(false);
            }


                handler.postDelayed(this,100);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address1);
        saveAdd=(TextView)findViewById(R.id.saveAdd);
        savePhone=(TextView)findViewById(R.id.savephone);
        btn=findViewById(R.id.button);
        total=findViewById(R.id.totalmoney);
        pay=findViewById(R.id.pay);
        pin=findViewById(R.id.pin);
        chngePin=findViewById(R.id.ButtonPin);
        date=(EditText)findViewById(R.id.date);
        setDate Date1 = new setDate(this, R.id.date);
        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences1.edit();


        back=findViewById(R.id.back);
        page.run();
        


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        preferences3 = PreferenceManager.getDefaultSharedPreferences(this);
        num = preferences3.getString("orderId", "");



        ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);




        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();
                product1.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //  Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();

                    cartItem p = dataSnapshot1.getValue(cartItem.class);
                    product1.add(p);
                    // notify();
                }
                for (int i=0;i<product1.size();i++){
                    rate=rate+(product1.get(i).getTotal());
                }
                if(rate>400 && rate<=1000 ) {
                    rate=rate+10;
                }

                else if(rate>=100 && rate<=400 ) {
                    rate=rate+20;
                }
                else if (rate>50 && rate<=100){
                    rate=rate+40;
                }
                else if (rate<=50){
                    rate=rate+100;
                }

                total.setText(String.valueOf("₹ "+rate));


            }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

                }
            });


            ref= FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("add");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    val1=dataSnapshot.getValue().toString();
                }
                saveAdd.setText(val1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref= FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ad=dataSnapshot.getValue().toString();
                }
                savePhone.setText(ad);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(date.getText().toString().isEmpty()){
                    Toast.makeText(Address1.this,"Please Enter Delivery Date",Toast.LENGTH_SHORT).show();
                }
                else if(saveAdd.getText().toString().equals("")||saveAdd.getText().toString().length()==0){
                    Toast.makeText(Address1.this,"Please Enter Delivery Address",Toast.LENGTH_SHORT).show();
                }
                else if(pin.getText().toString().isEmpty()){
                    Toast.makeText(Address1.this,"Please Enter Pin code",Toast.LENGTH_SHORT).show();
                }
                else {

                    ref=FirebaseDatabase.getInstance().getReference("PinCode");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                String str=dataSnapshot1.getKey().toString();
                                ppn.add(str);
                              pinCode ob=dataSnapshot1.getValue(pinCode.class);
                              pinList.add(ob);
                            }
                            //Toast.makeText(Address1.this,""+pinList.get(0).getPincode(),Toast.LENGTH_LONG).show();
                            if(ppn.contains(pin.getText().toString())) {
                                Log.d("msf",pin.getText().toString());
                                ref=FirebaseDatabase.getInstance().getReference("PinCode").child(pin.getText().toString().trim()).child("status");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            String val = snapshot.getValue().toString();
                                            if (val.equals("on")) {
                                                Intent intent = new Intent(Address1.this, PaymentOption.class);
                                                //ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child("Item").child("YourOrder").child(num).child("Date");
                                                //ref.setValue(rate);
                                                intent.putExtra("payTotal", String.valueOf(rate));
                                                intent.putExtra("Date", date.getText().toString());
                                                intent.putExtra("Add", val1);
                                                //Toast.makeText(Address1.this,""+date.getText().toString(),Toast.LENGTH_LONG).show();

                                                startActivity(intent);
                                            } else {
                                                pin.setError("Delivery in this location currently not available");
                                                Toast.makeText(Address1.this, "Please Change pin Code ", Toast.LENGTH_SHORT).show();
                                                pin.setFocusableInTouchMode(true);
                                                pin.setFocusable(true);
                                                pin.requestFocus();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else{
                                pin.setError("Delivery in this location currently not available");
                                Toast.makeText(Address1.this,"Please Change pin Code ",Toast.LENGTH_SHORT).show();
                                pin.setFocusableInTouchMode(true);
                                pin.setFocusable(true);
                                pin.requestFocus();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                pay.setBackgroundResource(R.drawable.background7);
            }

        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Address1.this,AddressPage.class);
                startActivity(intent);
                btn.setBackgroundResource(R.drawable.background7);
                btn.setTextColor(getResources().getColor(R.color.black));
            }
        });

        ref=FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("pincode");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String str=dataSnapshot.getValue().toString();
                    pin.setText(str);
                }
                else{
                    pin.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chngePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin.setFocusableInTouchMode(true);
                pin.setFocusable(true);
                chngePin.setBackgroundResource(R.drawable.background7);
                if(pin.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(pin, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });


    }

    public  void  check(ArrayList<pinCode> pinList){
        if(pinList.contains(pin.getText().toString())){
            ref=ref.child("PinCode").child(pin.getText().toString()).child("status");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() ){
                        String val=snapshot.getValue().toString();
                        if(val.equals("on")){
                            Intent intent = new Intent(Address1.this, PaymentOption.class);
                            //ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child("Item").child("YourOrder").child(num).child("Date");
                            //ref.setValue(rate);
                            intent.putExtra("payTotal", String.valueOf(rate));
                            intent.putExtra("Date", date.getText().toString());
                            intent.putExtra("Add", val);
                            //Toast.makeText(Address1.this,""+date.getText().toString(),Toast.LENGTH_LONG).show();

                            startActivity(intent);
                        }
                        else{
                            pin.setError("Delivery in this location currently not available");
                            Toast.makeText(Address1.this,"Please Change pin Code ",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            pin.setError("Delivery in this location currently not available");
            Toast.makeText(Address1.this,"Please Change pin Code ",Toast.LENGTH_SHORT).show();
        }


       /*
        }*/
    }


}
