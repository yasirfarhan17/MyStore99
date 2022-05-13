package com.noor.mystore99.amigrate.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter : ListAdapter<CartEntity, CartAdapter.CartViewHolder>(UserComparator) {


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
        val user = getItem(position)
        holder.bind(user)
    }

    fun clearAdapter() {
        submitList(emptyList())
    }

}

object UserComparator : DiffUtil.ItemCallback<CartEntity>() {
    override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem.products_name == newItem.products_name
    }

    override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem == newItem
    }
}