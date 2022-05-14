package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter(private val callback: CartAdapterCallback) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

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


    inner class CartViewHolder(
        private val binding: IndiviewCartBinding,
        onItemClick: (position: Int, increase: Boolean, weightType: Boolean) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            Log.d("SAHIL", "cartViewHolder")
            binding.bt1kg.setOnClickListener { onItemClick(adapterPosition, true, true) }
            binding.bt250gm.setOnClickListener { onItemClick(adapterPosition, true, true) }
            binding.bt500gm.setOnClickListener { onItemClick(adapterPosition, true, true) }
            binding.btIncrease.setOnClickListener { onItemClick(adapterPosition, true, false) }
            binding.btDecrease.setOnClickListener { onItemClick(adapterPosition, false, false) }
        }

        fun bind(item: CartEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvCurrentCount.text = item.count.toString()
                tvProductPrice.text = item.price.toString()
                "${item.count} * ${item.price}".also { tvQuantity.text = it }
                tvAmount.text = (item.count * (item.price?.toIntOrNull() ?: 0)).toString()
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
        val holder = CartViewHolder(binding) { adapterPosition, increase, weighttype ->
            if (weighttype) {
                callback.onWeightTypeItemClick(
                    items[adapterPosition].toProductEntity(),
                    adapterPosition
                )
                return@CartViewHolder
            }
            if (increase) {
                callback.onIncreaseItemClick(
                    items[adapterPosition].toProductEntity(),
                    adapterPosition
                )
                return@CartViewHolder
            } else {
                callback.onDecreaseItemClick(
                    items[adapterPosition].toProductEntity(),
                    adapterPosition
                )
                return@CartViewHolder
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}

interface CartAdapterCallback {
    fun onIncreaseItemClick(item: ProductEntity, position: Int)
    fun onDecreaseItemClick(item: ProductEntity, position: Int)
    fun onWeightTypeItemClick(item: ProductEntity, position: Int)
}
