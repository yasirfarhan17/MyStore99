package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.checkOutModel
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.checkout.CheckoutAdapter
import com.noor.mystore99.databinding.IndiviewCheckoutBinding
import com.noor.mystore99.databinding.IndiviewMyorderBinding

class MyOrderAdapter : RecyclerView.Adapter<MyOrderAdapter.MYOrderViewHolder>() {
    private val items = ArrayList<checkOutModel>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<checkOutModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    inner class MYOrderViewHolder(private val binding: IndiviewMyorderBinding):RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item:checkOutModel){
            with(binding){
                tvAmount.text="₹ "+item.amount
                tvDate.text="Delivery Date"+item.date
                tvItem.text="${item.list?.size.toString()}items"
                tvOrderId.text= "OrderId:-"+item.orderId
                //tvPayment.text="Not Paid"
                tvStatus.text="Status:-delivered"
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MYOrderViewHolder {
        val binding=
            IndiviewMyorderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MYOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MYOrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =items.size
}