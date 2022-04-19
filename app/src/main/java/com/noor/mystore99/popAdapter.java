




    package com.noor.mystore99;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class popAdapter   extends RecyclerView.Adapter<popAdapter.ViewHolder> {
        private List<popData> list;
        DatabaseReference ref;
        ArrayList<product> product1=new ArrayList<>();

        public popAdapter(List<popData> categoryModelList,ArrayList<product> a) {
            product1=a;
            this.list = categoryModelList;
        }

        @NonNull
        @Override
        public popAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull popAdapter.ViewHolder holder, final int position) {

            holder.popPrice.setText("₹ "+list.get(position).getRate());
            holder.popWeight.setText(" / "+list.get(position).getWeight());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  ref=FirebaseDatabase.getInstance().getReference("update").child(product1.get(position).getProducts_name()).child("rate");
                  ref.setValue(list.get(position).getRate());
                    ref=FirebaseDatabase.getInstance().getReference("update").child(product1.get(position).getProducts_name()).child("weight");
                    ref.setValue(list.get(position).getWeight());

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        public  class ViewHolder extends RecyclerView.ViewHolder{

            private TextView popPrice,popWeight;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                popPrice=itemView.findViewById(R.id.popPrice);
                popWeight=itemView.findViewById(R.id.popWeight);

            }

        }
    }
