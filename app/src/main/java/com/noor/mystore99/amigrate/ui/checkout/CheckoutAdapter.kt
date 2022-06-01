package com.noor.mystore99.amigrate.ui.checkout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewCheckoutBinding

class CheckoutAdapter :RecyclerView.Adapter<CheckoutAdapter.CheckOutViewHolder>() {

    private val items = ArrayList<CartModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CartModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    inner class CheckOutViewHolder(private val binding:IndiviewCheckoutBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item:CartModel){
            with(binding){
                finalName.text=item.products_name
                lastPrice.text="₹ "+item.price
                finalAmount.text="₹ "+item.total
                lastQuantity.text=" x "+item.quant
                item.img!!.decodeToBitmap(500)?.let {
                    imgCheckout.load(it) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        val binding=IndiviewCheckoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CheckOutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =items.size
}