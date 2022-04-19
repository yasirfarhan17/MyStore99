package com.noor.mystore99;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class categoryAdaptor  extends RecyclerView.Adapter<categoryAdaptor.ViewHolder> {
    private List<categoryModel> categoryModelList;
    Context context;

    public categoryAdaptor(List<categoryModel> categoryModelList,Context c) {
        this.categoryModelList = categoryModelList;
        context=c;
    }

    @NonNull
    @Override
    public categoryAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoryModelList.get(position).getCategoryName());
       // Picasso.get().load(categoryModelList.get(position).getCategoryIconLink()).fit().into(holder.categoryIcon);
        byte[] decodedString = Base64.decode(categoryModelList.get(position).getCategoryIconLink(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.categoryIcon.setImageBitmap(null);
        Glide
                .with(context.getApplicationContext()).
                load(decodedByte)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(new ObjectKey(System.currentTimeMillis()))
                        .override(200,200)
                        .placeholder(R.drawable.ic_home_black_24dp))
                .into(holder.categoryIcon);

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView categoryIcon;
        private TextView categoryName;
        View root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon= itemView.findViewById(R.id.category_icon);
            categoryName=itemView.findViewById(R.id.category_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(),""+categoryModelList.get(getAdapterPosition()).getCategoryName(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),CategoryProduct.class);
                    intent.putExtra("index",getAdapterPosition());
                    intent.putExtra("name",categoryModelList.get(getAdapterPosition()).getCategoryName());
                    view.getContext().startActivity(intent);
                }
            });




        }
        private  void setCategoryIcon(){
            ///set categoryIcon here
        }

        private  void setCategoryName(String name){
            ///set categoryName here
            categoryName.setText(name);
        }
    }
}
