package com.noor.mystore99;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class upiPay extends AppCompatActivity {
    EditText amount, note, name, upivirtualid;
    Button send;
    String TAG ="main";
    final int UPI_PAYMENT = 0;
    DatabaseReference ref;
    SharedPreferences preferences;
    String key,val,ad,date,name1,phone,add,num,op ,payToatl;
    private ArrayList<cartItem> list=new ArrayList<>();

    final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    final String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ad=getIntent().getStringExtra("PayTotal");
        date=getIntent().getStringExtra("Date");
        add=getIntent().getStringExtra("address");
        payToatl=getIntent().getStringExtra("payTotal");

        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("total");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    val=dataSnapshot.getValue().toString();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(upiPay.this,val,Toast.LENGTH_SHORT);

        payUsingUpi("SabziTaza", "9117151927@okbizaxis",
                "Sabzi Taza Payment", ad);



    }
    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("tr", "261433")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(upiPay.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(upiPay.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(upiPay.this, "Transaction successful.", Toast.LENGTH_SHORT).show();


                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key);

                final String finalApprovalRefNo = approvalRefNo;
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();
                        list.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            //  Toast.makeText(CartProductList.this,"hii",Toast.LENGTH_SHORT).show();

                            cartItem p = dataSnapshot1.getValue(cartItem.class);
                            list.add(p);

                            // notify();
                        }
                        Random rnd = new Random();
                        int number = rnd.nextInt(99999999);
                        num = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                        final String currentDate1 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                        final String currentTime1 = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
                        String combo=currentDate1+currentTime1;


                        ref = FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("Item").child("YourOrder").child(combo);
                        ref.setValue(list);
                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Payment");
                        ref.setValue("Payment Paid (UPI)");
                        op = "Payment Paid (UPI)";
                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Date");
                        ref.setValue(date);

                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("address");
                        ref.setValue(add);

                        ref = FirebaseDatabase.getInstance().getReference("User").child(key);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    name1 = dataSnapshot.child("name").getValue().toString();
                                    phone = dataSnapshot.child("phone").getValue().toString();
                                }
                                ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("name");
                                ref.setValue(name1);
                                ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("phone");
                                ref.setValue(phone);

                                sendEmail(combo, list, date, add, name1, phone, ad, op);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("CurrentDate");
                        ref.setValue(currentDate);

                        ref = FirebaseDatabase.getInstance().getReference("OrderConfirm").child(combo).child("status");
                        ref.setValue("no");
                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Total");
                        ref.setValue(ad);

                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("Time");
                        ref.setValue(combo);

                        ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(combo).child("currentTime");
                        ref.setValue(currentTime);


                        ref = FirebaseDatabase.getInstance().getReference("Cart").child(key);
                        ref.removeValue();

                        ref = FirebaseDatabase.getInstance().getReference("status").child(key).child(combo).child("placed");
                        ref.setValue("ok");



                ref = FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child("Item").child(combo).child("ReferenceNo");
                ref.setValue(String.valueOf(finalApprovalRefNo));
                Log.e("UPI", "payment successfull: " + finalApprovalRefNo);

                Intent intent = new Intent(upiPay.this, LastPage.class);
                //Toast.makeText(PaymentOption.this,num,Toast.LENGTH_SHORT).show();
                intent.putExtra("OrderId", combo);
                startActivity(intent);
            }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

                    }
                });




            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(upiPay.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
                finish();
            }
            else {
                Toast.makeText(upiPay.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
                finish();
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(upiPay.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        finish();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void sendEmail(String order,ArrayList<cartItem> list,String date,String add,String name,String phone,String payToatl,String op) {
        //Getting content for email

        ArrayList<String> val=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getWeight().equals("Per Kg")){
                val.add("Item: "+list.get(i).getName()+"\t(1 Kg"+")"+"\t Quantity :"+list.get(i).getQuant()+"\t price: "+list.get(i).getTotal()+"\n");
            }

        }
        String str="Order"+order+"\t\t\t"+"Order Date:"+date+"Name "+name+"\t\t\t"+"Phone: "+phone+"\n"+val+"\n"+"Address :"+add+"\nTotal: "+payToatl+"\nPayment: "+op;
//Creating SendMail object
        SendMail sm = new SendMail(this, "sabzitaza90@gmail.com", "Order", str);

        //Executing sendmail to send email
        sm.execute();
    }
}
