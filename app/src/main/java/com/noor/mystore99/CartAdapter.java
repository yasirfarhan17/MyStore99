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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter <CartAdapter.MyViewHolder>{

    ArrayList<cartItem> product1=new ArrayList<>();
    ArrayList<String> Name=new ArrayList<>();
    ArrayList<String> b=new ArrayList<>();
    //int count=1;
    DatabaseReference ref;
    private productAdapter.OnItemClickListener mListner;
    ArrayList<Integer> val=new ArrayList<>();
    String str1="";int num;
    ArrayList<Integer> num1=new ArrayList<>();
   ArrayList<Integer> total=new ArrayList<>();
    ArrayList<lastData> m=new ArrayList<>();


    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    SharedPreferences preferences;
    SharedPreferences.Editor editor ;
    String key;
    int rate=0,bVal=0,count=0;
    Context context;



    //View view1;

    public  int total1;
    Handler handler=new Handler();




    public CartAdapter(){

    }



    public CartAdapter(ArrayList<cartItem> p,Context c){
        context=c;
        product1=p;

        for(int i=0;i<getItemCount();i++){
            val.add(product1.get(i).getQuant());
            total.add(i);


        }
    }




    public void setOnItemClickListener(productAdapter.OnItemClickListener listener){
        mListner=listener;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view,parent,false);
        MyViewHolder evh=new MyViewHolder(v,mListner);
        //view1=v;

        return  evh;
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if(product1.get(position).getName()!=null) {

            preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            key = FirebaseAuth.getInstance().getCurrentUser().getUid();
            holder.name.setText(product1.get(position).getName());
            holder.price.setText("₹ "+product1.get(position).getPrice());
            holder.weight.setText("Weight:"+product1.get(position).getWeight());
            //holder.qunt.setText("Quantity :   "+String.valueOf(product1.get(position).getQuant()));


            Name.add(product1.get(position).getName());

           // MainActivity.update_counter(product1.size());
            //Toast.makeText(context.getApplicationContext(),""+product1.size(),Toast.LENGTH_SHORT).show();

            holder.t1.setText(String.valueOf(val.get(position)));

          /*  String str = product1.get(position).getPrice();
            if (str != null) {
                for (int i = 0; i < str.length(); i++) {
                    str1 = str1 + str.charAt(i);
                }
            }*/
            //
//        num= Integer.parseInt(str1);
            // num1.set(position,num);

            /*if(product1.get(position).getQuant()==500){
                holder.t1.setText(String.valueOf("500gm"));
                holder.t2.setText("₹ "+String.valueOf(Integer.parseInt(product1.get(position).getPrice())/2));

            }
            else if(product1.get(position).getQuant()==250){
                holder.t1.setText(String.valueOf("250gm"));
                holder.t2.setText("₹ "+String.valueOf(((Integer.parseInt(product1.get(position).getPrice())/2))/2));

            }
            else {*/
                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("total").setValue(val.get(position) * (Integer.parseInt(product1.get(position).getPrice())));
                holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
           // }





            str1 = "";
            //holder.setIsRecyclable(false);

            //holder.order.setVisibility(View.GONE);


            holder.setIsRecyclable(false);
        }


        //Toast.makeText(MainActivity.this,""+bVal,Toast.LENGTH_LONG).show();

        rate=0;

        for(int i=0;i<val.size();i++) {
            rate = rate + (val.get(i) * Integer.parseInt(product1.get(i).getPrice()));
        }

        CartProductList.update_counter(String.valueOf(rate));




        holder.dltButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);




                ref=FirebaseDatabase.getInstance().getReference("Cart");


                //

                Query name1= ref.child(key).orderByChild("name").equalTo(product1.get(position).getName());

                name1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        //Toast.makeText(v.getContext(),dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                        for(DataSnapshot rem:dataSnapshot.getChildren()){
                            rem.getRef().removeValue();
                        }


                        // product1.remove(getAdapterPosition());
                        // product1.clear();






                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                product1.remove(position);
                Name.remove(position);
                val.remove(position);
                //Toast.makeText(v.getContext()," "+product1, Toast.LENGTH_LONG).show();
                notifyItemRemoved(position);
                notifyDataSetChanged();
               // MainActivity.update_counter(product1.size());
                bVal=0;

                ref= FirebaseDatabase.getInstance().getReference("Cart").child(key);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            bVal=(int)dataSnapshot.getChildrenCount();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(v.getContext()," "+product1.size(), Toast.LENGTH_LONG).show();
               MainActivity.update_counter(product1.size());


            }
        });



        ref=FirebaseDatabase.getInstance().getReference("variety").child(product1.get(position).getName());
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int val=(int)dataSnapshot.getChildrenCount();
                count=val;

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
        });


        ref=FirebaseDatabase.getInstance().getReference("variety").child(product1.get(position).getName());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    holder.vBt1.setVisibility(View.VISIBLE);
                }
                if(dataSnapshot.child("500gm").exists()){
                   holder.vBt2.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("250gm").exists()){
                   holder.vBt3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.vBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.weight.setText("Weight:Per Kg");
                if(product1.get(position).getWeight().equals("500gm") ||product1.get(position).getWeight().equals("500Gm") ){
                    holder.price.setText("₹ "+Integer.parseInt(product1.get(position).getPrice())*2);
                    String str=String.valueOf(holder.price.getText());
                    String str1=str.substring(2,str.length());
                    product1.get(position).setPrice(str1);
                }
                else if(product1.get(position).getWeight().equals("250gm") ||product1.get(position).getWeight().equals("250Gm")){
                        holder.price.setText("₹ "+Integer.parseInt(product1.get(position).getPrice())*4);
                    String str=String.valueOf(holder.price.getText());
                    String str1=str.substring(2,str.length());
                    product1.get(position).setPrice(str1);

                }
                product1.get(position).setWeight("Per Kg");
                holder.vBt1.setEnabled(false);
                holder.vBt2.setEnabled(true);
                holder.vBt3.setEnabled(true);
                String str=String.valueOf(holder.price.getText());
                String str1=str.substring(2,str.length());
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("price").setValue(str1);
                ref.child("weight").setValue("Per kg");
                product1.get(position).setWeight("Per Kg");
                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("total").setValue(val.get(position) * (Integer.parseInt(product1.get(position).getPrice())));
                holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
                rate=0;

                for(int i=0;i<val.size();i++) {
                    rate = rate + (val.get(i) * Integer.parseInt(product1.get(i).getPrice()));
                }

                CartProductList.update_counter(String.valueOf(rate));
                holder.vBt1.setBackgroundResource(R.drawable.cartbg1);
                holder.vBt2.setBackgroundResource(R.drawable.cartbg);
                holder.vBt3.setBackgroundResource(R.drawable.cartbg);

            }
        });



        holder.vBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.weight.setText("Weight:500gm");
                holder.vBt1.setEnabled(true);
                holder.vBt2.setEnabled(false);
                holder.vBt3.setEnabled(true);

                ref=FirebaseDatabase.getInstance().getReference("variety").child(product1.get(position).getName()).child("500gm").child("rate");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String val1=dataSnapshot.getValue().toString();
                            holder.price.setText("₹ "+String.valueOf(val1));
                            ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                            ref.child("price").setValue(val1);
                            ref.child("weight").setValue("500gm");
                            product1.get(position).setWeight("500gm");
                            product1.get(position).setPrice(val1);
                            holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
                            ref = FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                            ref.child("total").setValue(val.get(position) * (Integer.parseInt(product1.get(position).getPrice())));

                            rate=0;

                            for(int i=0;i<val.size();i++) {
                                rate = rate + (val.get(i) * Integer.parseInt(product1.get(i).getPrice()));
                            }

                            CartProductList.update_counter(String.valueOf(rate));


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.vBt2.setBackgroundResource(R.drawable.cartbg1);
                holder.vBt1.setBackgroundResource(R.drawable.cartbg);
                holder.vBt3.setBackgroundResource(R.drawable.cartbg);


            }
        });

        holder.vBt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.weight.setText("Weight:250gm");
                ref=FirebaseDatabase.getInstance().getReference("variety").child(product1.get(position).getName()).child("250gm").child("rate");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String val1=dataSnapshot.getValue().toString();
                            holder.price.setText("₹ "+String.valueOf(val1));
                            ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                            ref.child("price").setValue(val1);
                            ref.child("weight").setValue("250gm");
                            product1.get(position).setWeight("250gm");
                            product1.get(position).setPrice(val1);
                            holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
                            ref = FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                            ref.child("total").setValue(val.get(position) * (Integer.parseInt(product1.get(position).getPrice())));
                            rate=0;

                            for(int i=0;i<val.size();i++) {
                                rate = rate + (val.get(i) * Integer.parseInt(product1.get(i).getPrice()));
                            }

                            CartProductList.update_counter(String.valueOf(rate));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.vBt1.setEnabled(true);
                holder.vBt2.setEnabled(true);
                holder.vBt3.setEnabled(false);

                //holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));


                holder.vBt3.setBackgroundResource(R.drawable.cartbg1);
                holder.vBt2.setBackgroundResource(R.drawable.cartbg);
                holder.vBt1.setBackgroundResource(R.drawable.cartbg);

            }
        });




       /* holder.vBt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.vBt1.setEnabled(false);
                holder.vBt2.setEnabled(true);
                holder.vBt3.setEnabled(true);
                if (product1.get(position).getWeight().equals("500gm")){

                }
                holder.t1.setText(String.valueOf(val.get(position)));
                holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("total").setValue(val.get(position) * (Integer.parseInt(product1.get(position).getPrice())));

            }
        });


        holder.vBt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.vBt1.setEnabled(true);
                holder.vBt3.setEnabled(true);
                holder.vBt2.setEnabled(false);
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("price").setValue(String.valueOf(Integer.parseInt(product1.get(position).getPrice())/2));
                notifyDataSetChanged();
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("weight").setValue("500gm");
                notifyDataSetChanged();
                holder.weight.setText("500gm");
            }
        });


        holder.vBt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.vBt1.setEnabled(true);
                holder.vBt2.setEnabled(true);
                holder.vBt3.setEnabled(false);
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("price").setValue(String.valueOf(((Integer.parseInt(product1.get(position).getPrice())/2))/2));
                notifyDataSetChanged();
                ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(position).getName());
                ref.child("weight").setValue("250gm");
                notifyDataSetChanged();
                holder.weight.setText("250gm");
                holder.price.setText("₹ "+product1.get(position).getPrice());
            }
        });*/





    }

    @Override
    public int getItemCount() {
        return product1.size();
    }



   /* public void buttonV(View v){

        for (int i = 0; i <= count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn = new Button(context);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText("button " + id_);
            btn.setBackgroundColor(Color.rgb(70, 80, 90));
            linear.addView(btn, params);
            holder.vBt1 = ((Button) v.findViewById(id_));
            holder.vBt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

    }*/





    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,ins_box,qunt,t1,t2,t3,weight;
        ImageView img,img1;
        Button btnIns,btnDsc,addBtn,dltButton,order,vBt1,vBt2,vBt3;
        CartProductList ob=new CartProductList();


        //final AdapterView.OnItemClickListener listener;

        public MyViewHolder(@NonNull final View itemView, final productAdapter.OnItemClickListener listener) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.CtTv1);
            price=(TextView)itemView.findViewById(R.id.CtTv2);
            t1=(TextView)itemView.findViewById(R.id.CtTv3);
            t2=(TextView)itemView.findViewById(R.id.CtTv4);
            btnDsc=(Button)itemView.findViewById(R.id.buttonDes);
            btnIns=(Button)itemView.findViewById(R.id.buttonIns);
            dltButton=(Button)itemView.findViewById(R.id.deletButton);
            weight=(TextView)itemView.findViewById(R.id.wt);
            vBt1=itemView.findViewById(R.id.vBt1);
            vBt2=itemView.findViewById(R.id.bt_500gm);
            vBt3=itemView.findViewById(R.id.bt_250gm);






            // view1=itemView;

            //total.add(val.get(getAdapterPosition())*(num1.get(getAdapterPosition())));
            //Toast.makeText(itemView.getContext()," "+total,Toast.LENGTH_LONG).show();









            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });




           btnIns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Toast.makeText(view.getContext(),""+val,Toast.LENGTH_SHORT).show();
                    preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                    key = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    int val1=val.get(getAdapterPosition());
                    val1=val1+1;
                    val.set(getAdapterPosition(),val1);
                    ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(getAdapterPosition()).getName());
                    ref.child("quant").setValue(val.get(getAdapterPosition()));


                    Toast.makeText(view.getContext(),"Successfully Update",Toast.LENGTH_SHORT).show();
                    t1.setText(String.valueOf(val.get(getAdapterPosition())));
                   t2.setText("Rs "+ String.valueOf(val.get(getAdapterPosition())*(Integer.parseInt(product1.get(getAdapterPosition()).getPrice()))));
                    rate=rate+(val.get(getAdapterPosition())*Integer.parseInt(product1.get(getAdapterPosition()).getPrice()));

                    CartProductList.update_counter(String.valueOf(rate));

                    ref.child("total").setValue(val.get(getAdapterPosition())*(Integer.parseInt(product1.get(getAdapterPosition()).getPrice())));
                    //notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();


                    //total.set(getAdapterPosition(),val.get(getAdapterPosition())*(Integer.parseInt(product1.get(getAdapterPosition()).getPrice())));
                    //Toast.makeText(context.getApplicationContext(),""+total,Toast.LENGTH_SHORT).show();

                }
            });


            btnDsc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   
                    if(val.get(getAdapterPosition())>1) {
                        preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                        key = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        int val1=val.get(getAdapterPosition());
                        val1=val1-1;
                        val.set(getAdapterPosition(),val1);
                        ref=FirebaseDatabase.getInstance().getReference("Cart").child(key).child(product1.get(getAdapterPosition()).getName());
                        ref.child("quant").setValue(val.get(getAdapterPosition()));

                        t1.setText(String.valueOf(val.get(getAdapterPosition())));
                        t2.setText("Rs "+ String.valueOf(val.get(getAdapterPosition())*(Integer.parseInt(product1.get(getAdapterPosition()).getPrice()))));
                        rate=rate+(val.get(getAdapterPosition())*Integer.parseInt(product1.get(getAdapterPosition()).getPrice()));

                        CartProductList.update_counter(String.valueOf(rate));

                        ref.child("total").setValue(val.get(getAdapterPosition())*(Integer.parseInt(product1.get(getAdapterPosition()).getPrice())));
                        notifyItemRemoved(getAdapterPosition());
                        notifyDataSetChanged();
                        Toast.makeText(view.getContext(),"Successfully Update",Toast.LENGTH_SHORT).show();

                    }


                }
            });
            for(int i=0;i<total.size();i++){
                total1=total1+total.get(i);
            }
            Intent intent = new Intent("ArrayValue");
            //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
            intent.putIntegerArrayListExtra("array",total);
            LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(intent);

            ref=FirebaseDatabase.getInstance().getReference("Cart");




        }
        }

}
