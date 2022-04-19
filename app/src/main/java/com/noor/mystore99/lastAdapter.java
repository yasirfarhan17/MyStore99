package com.noor.mystore99;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class lastAdapter extends RecyclerView.Adapter <com.noor.mystore99.lastAdapter.MyViewHolder> {

    //int count=1;
    private lastAdapter.OnItemClickListener mListner;
    DatabaseReference ref;
    ArrayList<lastData> pp1;
    ArrayList<String> quant=new ArrayList<>();
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
    SharedPreferences preferences1;
    String key;



    public lastAdapter(ArrayList<lastData> z){
        pp1=z;

    }




    public interface OnItemClickListener{
        void onItemClick(int position);
        void setText(TextView box, int val);

    }



    public void setOnItemClickListener(lastAdapter.OnItemClickListener listener){
        mListner=listener;
    }



    @NonNull
    @Override
    public lastAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.last_view,parent,false);
       lastAdapter.MyViewHolder evh=new lastAdapter.MyViewHolder(v,mListner);




        return  evh;
    }



    @Override
    public void onBindViewHolder(@NonNull final lastAdapter.MyViewHolder holder, final int position) {


        holder.name.setText(pp1.get(position).getName()+" ("+pp1.get(position).getWeight()+")");
        holder.price.setText("₹ "+Integer.parseInt(pp1.get(position).getPrice())*Integer.parseInt(pp1.get(position).getQuant() ));
        holder.pp.setText("₹ "+pp1.get(position).getPrice());
        holder.quant1.setText("x "+pp1.get(position).getQuant());



    }

    @Override
    public int getItemCount() {
        return pp1.size();
    }





    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,ins_box,name1,price1,pp,quant1;
        ImageView img,img1;
        Button btnIns,btnDsc,addBtn;

        //final AdapterView.OnItemClickListener listener;

        public MyViewHolder(@NonNull final View itemView, final lastAdapter.OnItemClickListener listener) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.finalName);
            price=(TextView)itemView.findViewById(R.id.finalAmount);
            pp=(TextView)itemView.findViewById(R.id.lastPrice);
            quant1=(TextView)itemView.findViewById(R.id.lastQuantity);

            //name.setText(Name.get(0));
            //price.setText(arr.get(0));


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
            //Toast.makeText(itemView.getContext(),"hii",Toast.LENGTH_LONG).show();


        }



    }
}
