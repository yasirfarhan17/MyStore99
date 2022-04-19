package com.noor.mystore99;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;

public class LastPage extends AppCompatActivity {
    DatabaseReference ref;
    Button cancleOrder;
    ArrayList<Integer> arr=new ArrayList<>();
    ArrayList<String> Name=new ArrayList<>();
    ArrayList<Integer> arr1=new ArrayList<>();
    ArrayList<String> Name1=new ArrayList<>();
    ArrayList<lastData> m=new ArrayList<>();
    ArrayList<String> quant=new ArrayList<>();
    RecyclerView recyclerView;
    lastAdapter adapter;
    ArrayList<String> data=new ArrayList<>();
    TextView finalTotal,add,lastPrice,lastQuant,fPrice,t4,orderId,date,t6;
    int a;
    String str=null,addd,val;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;

    SharedPreferences preferences2;
    SharedPreferences.Editor editor1 ;
    static  int flag=0;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    SharedPreferences preferences1;
    String key,str1,zz;
    ImageView qr;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    ArrayList<String> va=new ArrayList<>();
    int size;
     int qqq=0;
     String add1,p;
     Button back;
     ImageView confirmed,onDeliver,delivered;
    Handler handler=new Handler();
    String getTim;
    String timeSub,timeReplace,name,phone;



    private Runnable page=new Runnable() {
        @Override
        public void run() {
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            String timeSub=currentTime.substring(0,5);
            timeReplace=timeSub.replaceAll("[^a-zA-Z0-9]","");
             final int timeCon=Integer.parseInt(timeReplace);

            final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(zz).child("CurrentDate");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        getTim = snapshot.getValue().toString();
                        if(currentDate.equals(getTim)){
                            ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(zz).child("currentTime");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        getTim =snapshot.getValue().toString();
                                        String timeSub1=getTim.substring(0,5);
                                        String timeReplace1=timeSub1.replaceAll("[^a-zA-Z0-9]","");
                                        int timeCon1=Integer.parseInt(timeReplace1);
                                        // Log.d("msd",""+timeCon1+" "+timeCon);
                                        if((timeCon-timeCon1)>100){
                                            cancleOrder.setEnabled(false);
                                            cancleOrder.setBackgroundResource(R.drawable.background5);
                                        }

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            cancleOrder.setEnabled(false);
                            cancleOrder.setBackgroundResource(R.drawable.background5);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            handler.postDelayed(this,1000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_page);
        Name.clear();
        arr.clear();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Name1.clear();
        arr1.clear();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("rate", "");
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
         arr1 = gson.fromJson(json, type);

        String json1 = sharedPrefs.getString("name", "");
        Type type1 = new TypeToken<ArrayList<String>>() {}.getType();
         Name1 = gson.fromJson(json1, type1);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        val = FirebaseAuth.getInstance().getCurrentUser().getUid();

        str=getIntent().getStringExtra("add");
        str1=getIntent().getStringExtra("addd");
        add=findViewById(R.id.TvAddress);
        zz=getIntent().getStringExtra("OrderId");


        finalTotal=(TextView)findViewById(R.id.finalTotal);
        t4=(TextView)findViewById(R.id.paymentConfirm);
        orderId=(TextView)findViewById(R.id.ord);
        fPrice=(TextView)findViewById(R.id.fPrice);
        lastPrice=findViewById(R.id.lastPrice);
        lastQuant=findViewById(R.id.lastQuantity);
        qr=(ImageView)findViewById(R.id.qr);
        date=findViewById(R.id.DeliveryDate);
        back=findViewById(R.id.back);
        t6=findViewById(R.id.textView6);

        confirmed=(ImageView)findViewById(R.id.confirmed);
        onDeliver=(ImageView)findViewById(R.id.onDelivery);
        delivered=(ImageView)findViewById(R.id.delivered);
        cancleOrder=findViewById(R.id.cancleOrder);
        //

        final android.app.AlertDialog waitingtDialog = new SpotsDialog.Builder().setContext(LastPage.this).build();
        waitingtDialog.show();


        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView=findViewById(R.id.final_recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Toast.makeText(LastPage.this,zz,Toast.LENGTH_SHORT).show();

        orderId.setText("OrderId:-" +String.valueOf(zz));

        page.run();

        cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        LastPage.this);

                // set title
                alertDialogBuilder.setTitle("Cancel Order");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you really want to cancel order?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ref= FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Item").child("YourOrder").child(zz);
                                ref.removeValue();
                                ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(zz);
                                ref.removeValue();
                               Intent intent=new Intent(LastPage.this,OrderPage.class);
                               startActivity(intent);

                                ref=FirebaseDatabase.getInstance().getReference("User").child(key);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            name=dataSnapshot.child("name").getValue().toString();
                                            phone=dataSnapshot.child("phone").getValue().toString();
                                        }
                                        waitingtDialog.show();
                                        sendEmail(zz,name,phone);
                                        waitingtDialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                               Toast.makeText(LastPage.this,"Order cancel successfully",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });


        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Item").child("YourOrder").child(zz);
        m.clear();
        //Toast.makeText(LastPage.this,pp+" "+quant,Toast.LENGTH_SHORT).show();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String val=dataSnapshot.child("weight").getValue().toString();
                if(val.equals("Per Kg")||val.equals("per kg") ||val.equals("Per kg"))
                    val="1 Kg";

                    lastData ob=new lastData(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("price").getValue().toString(),dataSnapshot.child("quant").getValue().toString(),val);
                    m.add(ob);


                lastAdapter adapter=new lastAdapter(m);
                //adapter=new lastAdapter(Name1,arr1);

                recyclerView.setAdapter(adapter);


                a=0;
                code(m);
                size=m.size();
                waitingtDialog.dismiss();




                for(int i=0;i<m.size();i++){
                    //Toast.makeText(LastPage.this,""+arr1,Toast.LENGTH_SHORT).show();
                    a=a+(Integer.parseInt(m.get(i).getPrice())*Integer.parseInt(m.get(i).getQuant()));
                }
                finalTotal.setText(""+a);
                if(a>400 && a<=1000 ){
                    t6.setText(R.string.d1);
                    fPrice.setText("₹ "+(a+10));
                }
                else if(a>=100 && a<=400 ){
                    t6.setText(R.string.d);
                    fPrice.setText("₹ "+(a+20));
                }
                else if(a>50 && a<=100 ){
                    t6.setText(R.string.d3);
                    fPrice.setText("₹ "+(a+40));
                }
                else if(a<=50){
                    t6.setText(R.string.d4);
                    fPrice.setText("₹ "+(a+100));
                }

                else{
                    t6.setText(R.string.d2);
                    fPrice.setText("₹ "+(a));
                }


                preferences = PreferenceManager.getDefaultSharedPreferences(LastPage.this);
                editor = preferences.edit();
                editor.putInt("go", a);
                editor.apply();






                //Toast.makeText(LastPage.this,""+m,Toast.LENGTH_SHORT).show();


            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("add");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        addd = dataSnapshot.getValue().toString();
                        //Toast.makeText(LastPage.this, str + " " + addd, Toast.LENGTH_SHORT).show();
                        add.setText(addd);
                        preferences2 = PreferenceManager.getDefaultSharedPreferences(LastPage.this);
                        editor1 = preferences1.edit();
                        editor1.putString("add",addd);
                        editor1.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(zz);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.child("Payment").getValue().toString();
                    String val1=dataSnapshot.child("Date").getValue().toString();
                    t4.setText(val);
                    date.setText("DeliveryDate: "+val1);
                    editor.putString("pay",val);
                    editor.apply();
                }

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





        //Toast.makeText(this,""+arr+" "+Name,Toast.LENGTH_LONG).show();

        ref=FirebaseDatabase.getInstance().getReference("OrderDetail").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(zz).child("confirmed");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                        confirmed.setBackgroundResource(R.drawable.border1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref=FirebaseDatabase.getInstance().getReference("OrderDetail").child((FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(zz).child("OnDelivery");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    onDeliver.setBackgroundResource(R.drawable.border1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref=FirebaseDatabase.getInstance().getReference("OrderDetail").child((FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(zz).child("Delivered");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    delivered.setBackgroundResource(R.drawable.border1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void code(ArrayList<lastData> val){
        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        add1 = preferences1.getString("add", "");

        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        p = preferences1.getString("pay", "");


       while(qqq<val.size()) {
           va.add("\nName= "+val.get(qqq).getName() + " ,Quantity= " + val.get(qqq).getPrice() + "x" + val.get(qqq).getQuant() + " ,Total=  ₹  " + Integer.parseInt(val.get(qqq).getPrice()) * Integer.parseInt(val.get(qqq).getQuant()) + "\n");
           Log.d("Check", "" + qqq);
           qqq++;
       }

        //Toast.makeText(LastPage.this,""+p,Toast.LENGTH_LONG).show();

        qrgEncoder = new QRGEncoder(("OrderId= "+zz+"\nItem="+va+"\n Address=  \""+add1+"\"\n Payment="+p), null, QRGContents.Type.TEXT, 2500);
        bitmap = qrgEncoder.getBitmap();

        // Getting QR-Code as Bitmap

        // Setting Bitmap to ImageView
        qr.setImageBitmap(bitmap);


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(LastPage.this,OrderPage.class);
        startActivity(intent);
    }

    private void sendEmail(String order,String name,String phone) {
        //Getting content for email

        String str="Order"+order+"\t\t\t"+"Name "+name+"\t\t\t"+"Phone: "+phone;
//Creating SendMail object
        SendMail sm = new SendMail(this, "sabzitaza90@gmail.com", "Order Cancel", str);

        //Executing sendmail to send email
        sm.execute();
    }

}
