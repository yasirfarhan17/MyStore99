package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.noor.mystore99.R
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter(
    val callback:CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val items = ArrayList<CartEntity>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CartEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapter() {
        items.clear()
        notifyDataSetChanged()
    }


    inner class CartViewHolder(private val binding: IndiviewCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvCartQuant.text = item.weight
                tvCartType.text = "₹ "+item.price
                tvCurrentQuant.text=item.quant
                total.text = "₹ "+item.total
                    imgCart.load(item.img) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_home_black_24dp)
                    }
                btIncrease.setOnClickListener {
                    item.quant= (item.quant?.toInt()?.plus(1)).toString()
                    item.total= (item.price?.toInt()?.times(item.quant!!.toInt())).toString()
                    callback.onClick(item.total!!,item.products_name, item.quant!!)
                    tvCurrentQuant.text=item.quant
                    total.text="₹ " + item.total
                   // CartActivity.subValue =CartActivity.subValue + item.total!!.toInt()
                    //notifyDataSetChanged()
                }
                btMinus.setOnClickListener {
                    if(item.quant!!.toInt() > 1) {
                        item.quant = (item.quant?.toInt()?.minus(1)).toString()
                        item.total = (item.price?.toInt()?.times(item.quant!!.toInt())).toString()
                        callback.onClick(item.total!!, item.products_name, item.quant!!)
                        tvCurrentQuant.text = item.quant
                        total.text = "₹ " + item.total
                       // CartActivity.subValue =CartActivity.subValue - item.total!!.toInt()
                    }
                    //notifyDataSetChanged()
                }
                imgClear.setOnClickListener {
                    callback.onDelete(item.products_name,position,item)
                    notifyItemRemoved(position)
                    items.removeAt(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding =
            IndiviewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(items[position])

    }

    override fun getItemCount(): Int = items.size
}

interface cartCallBack{
    fun onClick(price:String,id:String,quant:String)
    fun onDelete(id: String, pos: Int, item: CartEntity,)
}