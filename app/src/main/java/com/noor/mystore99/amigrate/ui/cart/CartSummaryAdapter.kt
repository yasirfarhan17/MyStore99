package com.noor.mystore99.amigrate.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.networkmodule.database.entity.CartEntity
import com.noor.mystore99.databinding.ItemCartSummaryBinding

class CartSummaryAdapter(private val items: List<CartEntity>) :
    RecyclerView.Adapter<CartSummaryAdapter.SummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding =
            ItemCartSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SummaryViewHolder(private val binding: ItemCartSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartEntity) {
            binding.tvSummaryName.text = item.products_name
            binding.tvSummaryDetails.text = "${item.weight} x ${item.quant}"
            binding.tvSummaryPrice.text = "₹ ${item.total}"
        }
    }
}
