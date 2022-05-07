package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.noor.mystore99.amigrate.util.extension.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

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
                tvCartQuant.text = item.quant
                tvCartType.text = item.price
                total.text = item.price
                item.img!!.decodeToBitmap(500)?.let {
                    imgCart.load(it) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            IndiviewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(items[position])

    }

    override fun getItemCount(): Int = items.size
}