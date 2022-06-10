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
import com.noor.mystore99.R
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
                    imgCheckout.load(item.img) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_home_black_24dp)
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.CheckOutViewHolder {
        val binding=IndiviewCheckoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CheckOutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckOutViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =items.size
}