package com.noor.mystore99.amigrate.ui.main.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.R
import com.noor.mystore99.databinding.CardviewBinding
import com.noor.mystore99.productModel

class UserAdapter(val context: UserFragment):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val item=ArrayList<ProductEntity>()
    private val itemFilter=ArrayList<ProductEntity>()



    @SuppressLint("NotifyDataSetChange")
    fun submitList(list: ArrayList<ProductEntity>){
        item.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        item.addAll(list)
        notifyDataSetChanged()
    }
    inner class  UserViewHolder(private val binding: CardviewBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(item:ProductEntity){
                    with(binding){
                        name.text=item.products_name
                        price.text=item.price
                        hindiName.text=item.HindiName



                        val decodedString: ByteArray =
                            Base64.decode(item.img, Base64.DEFAULT)
                        val decodedByte =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        Glide
                            .with(context.requireContext())
                            .asBitmap()
                            .load(decodedByte)
                            .dontTransform()
                            .apply(
                                RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .signature(ObjectKey(System.currentTimeMillis()))
                                    .override(150, 150)
                                    .placeholder(R.drawable.ic_home_black_24dp)
                            )
                            .into(productImg)

                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding=CardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int =item.size


}