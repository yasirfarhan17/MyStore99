package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.networkmodule.model.checkOutModel
import com.noor.mystore99.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.*

class MyOrderAdapter(
    private val callback: MyOrderCallBack
) : RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {
    
    private val items = ArrayList<checkOutModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<checkOutModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: checkOutModel) {
            with(binding) {
                // Order ID
                tvOrderId.text = "Order #${item.orderId}"
                
                // Date formatting
                tvDate.text = formatDate(item.date)
                
                // Total amount
                tvTotal.text = "₹${item.amount}"
                
                // Status (default to "Delivered" since model doesn't have status field)
                tvStatus.text = "Delivered"
                
                // Click listener
                root.setOnClickListener {
                    callback.onItemClick(item.orderId.toString())
                }
            }
        }
        
        private fun formatDate(dateString: String?): String {
            return try {
                dateString ?: "N/A"
            } catch (e: Exception) {
                "N/A"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

interface MyOrderCallBack {
    fun onItemClick(orderId: String)
}