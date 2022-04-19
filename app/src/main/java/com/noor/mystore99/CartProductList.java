package com.noor.mystore99;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartProductList extends AppCompatActivity {

    RecyclerView recyclerView1;
    DatabaseReference ref;
    ArrayList<cartItem> list;
    ArrayList<Integer> arr1=new ArrayList<>();
    CartAdapter adapter;
    public  static TextView t3;
    ArrayList<lastData> m=new ArrayList<>();
    Button order;
    Dialog mydialog;
    Button dilogButton,back;
    RadioGroup rg;
    RadioButton rb;
    String key;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    SharedPreferences preferences ;
    int pri,a,rate;
    Handler handler=new Handler();
    static int vall=0;
    //String key;

   private Runnable page=new Runnable() {
        @Override
        public void run() {

            ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);




            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    a=(int)dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

                }
            });
            if(a==0){
                order.setEnabled(false);
                t3.setText("total");
            }
            else {
                order.setEnabled(true);
            }
            handler.postDelayed(this,100);

        }
    };








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product_list);
        recyclerView1=findViewById(R.id.cart_recyclerView1);
        //t3=findViewById(R.id.totalPrice);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(CartProductList.this));
        list=new ArrayList<cartItem>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();;
        pri = preferences.getInt("value", 1);
        order=(Button)findViewById(R.id.orderId);
        t3=findViewById(R.id.tot);
        back=findViewById(R.id.back);
        page.run();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


       // t3.setText(String.valueOf(pri));


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

                   // notify();se
                }
                //a=(int)dataSnapshot.getChildrenCount();


                adapter=new CartAdapter(list,CartProductList.this);
                adapter.notifyDataSetChanged();
                recyclerView1.setAdapter(adapter);
                pri=0;

                for(int i=0;i<list.size();i++){
                    pri=pri+(list.get(i).getQuant())*Integer.parseInt(list.get(i).getPrice());
                }

               // page.run();
               //pri=preferences.getInt("value",1);
               // t3.setText(String.valueOf(pri));



               // recreate();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(),"Oppps....someThing is wrong",Toast.LENGTH_SHORT).show();

            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ArrayValue"));


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                //Toast.makeText(CartProductList.this,""+a,Toast.LENGTH_SHORT).show();
                if(vall!=0) {
                    if (vall < 100) {
                        Toast.makeText(CartProductList.this, "Total amount is very less please add more item", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(CartProductList.this, Address1.class);
                        startActivity(intent);
                    }
                }

               order.setBackgroundResource(R.drawable.background7);


            }
        });



    }
    public static  void update_counter(String value){
        try{
            if(Integer.parseInt(value)>400 && Integer.parseInt(value)<=1000 ) {
                t3.setText("₹ " + value + "+10");
                vall=Integer.parseInt(value)+10;
            }

            else if(Integer.parseInt(value)>=100 && Integer.parseInt(value)<=400 ) {
                t3.setText("₹ " + value + "+20");
                vall=Integer.parseInt(value)+20;
            }
            else {
                t3.setText("₹ " + value);
                vall=Integer.parseInt(value);
            }
        }
        catch (Exception ex){
            Log.d("Exception","Exception of type"+ex.getMessage());
        }
    }






    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ArrayList<Integer> list1 = intent.getIntegerArrayListExtra("array");
            //String qty = intent.getStringExtra("quantity");
            //Toast.makeText(CartProductList.this,""+list1  ,Toast.LENGTH_SHORT).show();
        }
    };

}
