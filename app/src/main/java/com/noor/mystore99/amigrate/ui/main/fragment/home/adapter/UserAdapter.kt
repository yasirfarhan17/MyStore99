package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.ProductModelNew
import com.example.networkmodule.util.Util.bitMapToString
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.MainActivity
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.cartItem
import com.noor.mystore99.databinding.IndiviewProductsBinding
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(
    val callBack: UserFragment
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val item = ArrayList<ProductEntity>()
    private val itemCart = ArrayList<CartEntity>()
    private val itemFilter = ArrayList<ProductEntity>()



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ProductEntity>,cartItem:ArrayList<CartEntity>) {
        item.clear()
        itemFilter.clear()
        itemCart.clear()
        itemCart.addAll(cartItem)
        itemFilter.addAll(list)
        item.addAll(list)
        notifyDataSetChanged()
    }
    fun submitListNew(list: ArrayList<ProductEntity>) {
        item.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        item.addAll(list)
        notifyDataSetChanged()
    }


    inner class UserViewHolder(private val binding: IndiviewProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvMrp.text = item.price
                tvHindiName.text = item.HindiName
                imgProductImage.load(item.img) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_home_black_24dp)
                }
                if(item.stock.equals("no")){
                    btAddToCart.visibility=View.GONE
                    outofstock.visibility=View.VISIBLE
                    btMinus.visibility=View.GONE
                    btIncrease.visibility=View.GONE
                    tvCurrentQuant.visibility=View.GONE
                }
                else{
                    btAddToCart.visibility=View.VISIBLE
                    outofstock.visibility=View.GONE
                    btMinus.visibility=View.GONE
                    btIncrease.visibility=View.GONE
                    tvCurrentQuant.visibility=View.GONE
                }

                itemCart.forEach {

                    if(it.products_name.equals(item.products_name))
                    {
                        Log.d("insideItemcart",""+it.products_name)
                        btAddToCart.visibility=View.GONE
                        btMinus.visibility=View.VISIBLE
                        btIncrease.visibility=View.VISIBLE
                        tvCurrentQuant.visibility=View.VISIBLE
                        tvCurrentQuant.text=it.quant
                    }
                }

                Log.d("insideAdapter"," ${item.img}")




                val cartEntity = CartEntity(item.products_name, item.price, item.img, item.quant,"1", item.price)
                btAddToCart.setOnClickListener {
                    callBack.onItemClick(cartEntity)
                    btAddToCart.visibility=View.GONE
                    btMinus.visibility=View.VISIBLE
                    btIncrease.visibility=View.VISIBLE
                    tvCurrentQuant.visibility=View.VISIBLE
                    //Toast.makeText(it.context, "Item Added successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            IndiviewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(itemFilter[position])
    }

    override fun getItemCount(): Int = itemFilter.size




}

interface UserAdapterCallBack{
    fun onItemClick(cartEntity: CartEntity)
    fun searchInCartDB(id:String)
}