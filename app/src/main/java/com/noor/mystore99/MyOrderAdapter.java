package com.noor.mystore99;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrderAdapter extends RecyclerView.Adapter <MyOrderAdapter.MyViewHolder>{

    //View view1;

    public  int total1;
    private productAdapter.OnItemClickListener mListner;
    Handler handler=new Handler();
    ArrayList<String> product1=new ArrayList<>();
    DatabaseReference ref;
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String key;
    Context context;
    ArrayList<Long> time=new ArrayList<>();
    int item;




    public MyOrderAdapter(){

    }



    public MyOrderAdapter(ArrayList<String> p,Context c){
        product1=p;
        context=c;

    }




    public void setOnItemClickListener(productAdapter.OnItemClickListener listener){
        mListner=listener;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderview,parent,false);
        MyViewHolder evh=new MyViewHolder(v,mListner);
        //view1=v;

        return  evh;
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {



        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        key = FirebaseAuth.getInstance().getCurrentUser().getUid();



        holder.orderId.setText("Order Id: "+product1.get(position));

        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(product1.get(position)).child("Date");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();
                    holder.date.setText("Delivery Date:"+val);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(product1.get(position)).child("CurrentDate");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();
                    holder.currentDate.setText("Order Date:"+val);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(product1.get(position)).child("Payment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();
                    holder.t3.setText("Payment: "+val);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("Myorder").child(key).child("Item").child("YourOrder").child(product1.get(position));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   item=(int)dataSnapshot.getChildrenCount();

                    holder.t1.setText(item+" Items");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref= FirebaseDatabase.getInstance().getReference("OrderDetail").child(key).child(product1.get(position)).child("Total");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String val=dataSnapshot.getValue().toString();
                    holder.t2.setText("Amount :₹ "+val);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Intent intent=new Intent(v.getContext(),LastPage.class);
                intent.putExtra("OrderId",product1.get(position));
                v.getContext().startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return product1.size();
    }






    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, currentDate, qunt, t1, t2, t3, weight;
        ImageView img, img1;
        Button btnIns, btnDsc, addBtn, dltButton, order;
        CartProductList ob = new CartProductList();


        //final AdapterView.OnItemClickListener listener;

        public MyViewHolder(@NonNull final View itemView, final productAdapter.OnItemClickListener listener) {
            super(itemView);
            orderId = (TextView) itemView.findViewById(R.id.orderIDDD);
            date = (TextView) itemView.findViewById(R.id.LDate);
            currentDate = (TextView) itemView.findViewById(R.id.CDate);
            t1= (TextView) itemView.findViewById(R.id.orderItem);
            t2= (TextView) itemView.findViewById(R.id.orderAmount);
            t3= (TextView) itemView.findViewById(R.id.orderPayment);

        }

    }

}
