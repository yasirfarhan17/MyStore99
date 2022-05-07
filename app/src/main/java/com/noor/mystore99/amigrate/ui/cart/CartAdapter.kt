package com.noor.mystore99.amigrate.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.cart.CartEntity
import com.example.networkmodule.database.product.ProductEntity
import com.noor.mystore99.amigrate.util.extension.Util.decodeToBitmap
import com.noor.mystore99.databinding.ActivityNewCartBinding
import com.noor.mystore99.databinding.CartIndiViewBinding

class CartAdapter :RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val item = ArrayList<CartEntity>()
    private val itemFilter = ArrayList<CartEntity>()



    fun submitList(list : ArrayList<CartEntity>){
        item.clear()
        itemFilter.clear()
        item.addAll(list)
        itemFilter.addAll(list)
        notifyDataSetChanged()

    }



    inner class CartViewHolder(private val binding: CartIndiViewBinding)
        :RecyclerView.ViewHolder(binding.root) {

            fun bind(item:CartEntity){
                with(binding){
                    cartProductName.text=item.products_name
                    cartQuant.text=item.quant
                    cartType.text=item.price
                    total.text=item.price

                    item.img!!.decodeToBitmap(500)?.let {
                        cartImage.load(it) {
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=CartIndiViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(item[position])

    }

    override fun getItemCount(): Int=item.size
}