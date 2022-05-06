package com.noor.mystore99;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
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
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProductAdapter extends RecyclerView.Adapter <CategoryProductAdapter.MyViewHolder>{

    ArrayList<product> product1=new ArrayList<>();
    //int count=1;
    private OnItemClickListener mListner;
    DatabaseReference ref;
    ArrayList<String> val=new ArrayList<>();
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    SharedPreferences preferences ;
    String key;
    Context context;



    public CategoryProductAdapter(ArrayList<product> p,Context c){
        product1=p;
        context=c;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void setText(TextView box, int val);

    }



    public void setOnItemClickListener(OnItemClickListener listener){
        mListner=listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.indiview_products,parent,false);
        MyViewHolder evh=new MyViewHolder(v,mListner);

        return  evh;
    }



    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name.setText(product1.get(position).getProducts_name()+"\n("+product1.get(position).getHindiName()+")");


            holder.price.setText("₹ "+product1.get(position).getPrice());
        ref= FirebaseDatabase.getInstance().getReference("Add Mrp").child(product1.get(position).getProducts_name());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.price1.setVisibility(View.VISIBLE);
                    holder.textMrp.setVisibility(View.VISIBLE);
                    holder.price1.setText("₹ "+snapshot.getValue().toString());
                }
                else{
                    holder.price1.setVisibility(View.INVISIBLE);
                    holder.textMrp.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        byte[] decodedString = Base64.decode(product1.get(position).getImg(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide
                .with(context.getApplicationContext())
                .asBitmap()
                .load(decodedByte)
                .dontTransform()
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .override(150,150)
                        .placeholder(R.drawable.ic_home_black_24dp))
                .into(holder.img);

        //holder.btnIns.setTag(position);
        //holder.btnDsc.setTag(position);
        if(product1.get(position).getStock().equals("no")){
            holder.addBtn.setVisibility(View.GONE);
            holder.stock.setVisibility(View.VISIBLE);

        }
        else {
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.stock.setVisibility(View.GONE);
        }


        for(int i=0;i<getItemCount();i++){
            val.add("per dozen");
        }
        holder.ins_box.setText(product1.get(position).getQuant());

        //Toast.makeText(,"" +val,Toast.LENGTH_SHORT).show();
        ref=FirebaseDatabase.getInstance().getReference("Cart");

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                key = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(key!=null) {
                    cartItem obj = new cartItem(product1.get(position).getProducts_name(), product1.get(position).getPrice(),1,1,product1.get(position).getQuant());
                    ref.child(key).child(product1.get(position).getProducts_name()).setValue(obj);
                    Toast.makeText(view.getContext(), "Successfully added ", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return product1.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,ins_box,stock,price1,textMrp;
        ImageView img,img1;
        Button btnIns,btnDsc,addBtn;

        //final AdapterView.OnItemClickListener listener;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
          /*  name=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
            img=(ImageView) itemView.findViewById(R.id.product_img);
            ins_box=(TextView)itemView.findViewById(R.id.inc_box);
            stock=(TextView)itemView.findViewById(R.id.stock);
            //btnIns=(Button)itemView.findViewById(R.id.buttonIns);
            //btnDsc=(Button)itemView.findViewById(R.id.buttonDes);
            addBtn=(Button)itemView.findViewById(R.id.addBtn);
            price1=(TextView)itemView.findViewById(R.id.mrp);
            textMrp=(TextView)itemView.findViewById(R.id.textMrp);*/










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




        }



    }
}
