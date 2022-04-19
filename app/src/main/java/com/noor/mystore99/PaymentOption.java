package com.noor.mystore99;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noor.mystore99.Remote.IFCMService;
import com.noor.mystore99.Remote.RetrofitFCMClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;


public class PaymentOption extends AppCompatActivity {

    Button back;
    DatabaseReference ref;
    SharedPreferences preferences;
    String key,val,ad,payToatl,date,add;
    TextView total,t1;
    Spinner spinner;
    private ArrayList<cartItem> list=new ArrayList<>();
    private SpinnerAdapter mAdapter;
    Button pay;
    RadioGroup radioGroup;
    RadioButton radioButton1,radioButton2;
    static  int a=0;
    SharedPreferences preferences1;
    SharedPreferences.Editor editor ;
    String name,phone,op;
    IFCMService ifcmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        back=findViewById(R.id.back);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        total=(TextView)findViewById(R.id.payMon);
        t1=(TextView)findViewById(R.id.totalmoney);
        pay=(Button)findViewById(R.id.pay);
        radioGroup=findViewById(R.id.RadioGroup);
        radioButton1=findViewById(R.id.payRadio);
        radioButton2=findViewById(R.id.payRadio1);
        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        ifcmService= RetrofitFCMClient.getInstance().create(IFCMService.class);


        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences1.edit();
        payToatl=getIntent().getStringExtra("payTotal");
        date=getIntent().getStringExtra("Date");
        add=getIntent().getStringExtra("Add");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




                    total.setText("₹ "+payToatl);
                    t1.setText("₹ "+payToatl);


        ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();
                list.clear();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    //  Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();

                    cartItem p=dataSnapshot1.getValue(cartItem.class);
                    list.add(p);

                    // notify();
                }

                Spinner spinnerCountries = findViewById(R.id.paySpinner);
                mAdapter = new SpinnerAdapter(PaymentOption.this, list);
                spinnerCountries.setAdapter(mAdapter);
                spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cartItem clickedItem = (cartItem) parent.getItemAtPosition(position);
                        String clickedCountryName = clickedItem.getName();
                       // Toast.makeText(PaymentOption.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                radioButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Random rnd = new Random();
                        int number = rnd.nextInt(99999999);
                        //String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                        final String currentDate1 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                        final String currentTime1 = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                        String combo=currentDate1+currentTime1;

                        Log.d("TimeFrame",combo);
                        final String num = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                        editor.putString("orderId",combo);
                        editor.apply();



                       // Toast.makeText(PaymentOption.this,""+timeStamp,Toast.LENGTH_LONG).show();









                        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("Item").child("YourOrder").child(combo);
                        ref.setValue(list);
                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Payment");
                        ref.setValue("Payment Not Paid (COD)");
                        op="Payment Not Paid (COD)";
                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Date");
                        ref.setValue(date);

                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("address");
                        ref.setValue(add);

                        ref=FirebaseDatabase.getInstance().getReference("User").child(key);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    name=dataSnapshot.child("name").getValue().toString();
                                    phone=dataSnapshot.child("phone").getValue().toString();
                                }
                                ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("name");
                                ref.setValue(name);
                                ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("phone");
                                ref.setValue(phone);
                                final android.app.AlertDialog waitingtDialog = new SpotsDialog.Builder().setContext(PaymentOption.this).build();
                                waitingtDialog.show();
                               //prepareNotificationMessage(combo,name);


                                sendEmail(combo,list,date,add,name,phone,payToatl,op);
                                waitingtDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("CurrentDate");
                        ref.setValue(currentDate);

                        ref= FirebaseDatabase.getInstance().getReference("OrderConfirm").child(combo).child("status");
                        ref.setValue("no");
                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Total");
                        ref.setValue(payToatl);

                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Time");
                        ref.setValue(combo);

                        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("currentTime");
                        ref.setValue(currentTime);


                        ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);
                        ref.removeValue();

                        ref=FirebaseDatabase.getInstance().getReference("status").child(key).child(combo).child("placed");
                        ref.setValue("ok");

                        Intent intent=new Intent(PaymentOption.this,LastPage.class);
                        //Toast.makeText(PaymentOption.this,num,Toast.LENGTH_SHORT).show();
                        intent.putExtra("OrderId",combo);
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

            }
        });


                radioButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(PaymentOption.this,upiPay.class);
                        a++;
                        intent.putExtra("PayTotal",payToatl);
                        intent.putExtra("Date",date);
                        intent.putExtra("address",add);
                        startActivity(intent);

                    }
                });



    }
    private void sendEmail(String order,ArrayList<cartItem> list,String date,String add,String name,String phone,String payToatl,String op) {
        //Getting content for email

        ArrayList<String> val=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getWeight().equals("Per Kg")){
                val.add("Item: "+list.get(i).getName()+"\t(1 Kg"+")"+"\t Quantity :"+list.get(i).getQuant()+"\t price: "+list.get(i).getTotal()+"\n");
            }
            else{
                val.add("Item: "+list.get(i).getName()+"\t("+list.get(i).getWeight()+")"+"\t Quantity :"+list.get(i).getQuant()+"\t price: "+list.get(i).getTotal()+"\n");
            }

        }
String str="Order:"+order+"\t\t"+"Order-Date:"+date+"\nName :"+name+"\t\t\t\t"+"Phone: "+phone+"\n"+val+"\n"+"Address :"+add+"\nTotal: "+payToatl+"\nPayment: "+op;
//Creating SendMail object
        SendMail sm = new SendMail(this, "sabzitaza90@gmail.com", "Order", str);

        //Executing sendmail to send email
        sm.execute();
    }

  /*  private  void prepareNotificationMessage(String orderId,String name){
        String Notification_Topic="/topics/"+"PUSH_NOTIFICATION";
        String Notification_title="New order"+orderId;
        String Notificatio_message="You have new order from "+name;
        String Notification_type="NewOrder";

        JSONObject notification=new JSONObject();
        JSONObject notificationBody=new JSONObject();
        try{
            notificationBody.put("notificationType",Notification_type);
            notificationBody.put("BuyerUid",key);
            notificationBody.put("sellerUid",key);
            notificationBody.put("orderid",orderId);
            notificationBody.put("notficationTitle",Notification_title);
            notificationBody.put("notificationMesage",Notificatio_message);

            notification.put("to",Notification_Topic);
            notification.put("data",notificationBody);

        }
        catch(Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        sendFcmNotification(notificationBody,orderId,name);

    }

    private void sendFcmNotification(JSONObject notificationBody, String orderId, String name) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent=new Intent(PaymentOption.this,LastPage.class);
                //Toast.makeText(PaymentOption.this,num,Toast.LENGTH_SHORT).show();
                intent.putExtra("OrderId",orderId);
                startActivity(intent);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<>();
                header.put("Content-Type","application/json");
                header.put("Authorization","keys="+key);
                return header;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }*/
}
