package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
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
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.MainActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.databinding.IndiviewProductsBinding
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(
    val callBack: UserFragment
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val item = ArrayList<ProductEntity>()
    private val itemFilter = ArrayList<ProductEntity>()

    private lateinit var viewModel: UserViewModel


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ProductEntity>, viewModel: UserViewModel) {
        item.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        item.addAll(list)
        this.viewModel = viewModel
        notifyDataSetChanged()
    }


    inner class UserViewHolder(private val binding: IndiviewProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvMrp.text = item.price
                tvHindiName.text = item.HindiName
                imgProductImage.load(item.img?.decodeToBitmap()) {
                    transformations(CircleCropTransformation())
                }

                if(callBack.searchInCartDB(item.products_name.toString())){
                    btAddToCart.visibility=View.GONE
                    outofstock.visibility=View.GONE
                    btMinus.visibility=View.VISIBLE
                    btIncrease.visibility=View.VISIBLE
                    tvCurrentQuant.visibility=View.VISIBLE
                }

                if(item.stock.equals("no")){
                    btAddToCart.visibility=View.GONE
                    outofstock.visibility=View.VISIBLE
                }
                else{
                    btAddToCart.visibility=View.VISIBLE
                    outofstock.visibility=View.GONE
                    btMinus.visibility=View.GONE
                    btIncrease.visibility=View.GONE
                    tvCurrentQuant.visibility=View.GONE
                }

                val cartEntity =
                    CartEntity(item.products_name, item.price, item.img, item.quant,"1", item.price)
                btAddToCart.setOnClickListener {
                    //viewModel.insertToCartDb(cartEntity)
                    callBack.onItemClick(cartEntity)
                    btAddToCart.visibility=View.GONE
                    btMinus.visibility=View.VISIBLE
                    btIncrease.visibility=View.VISIBLE
                    tvCurrentQuant.visibility=View.VISIBLE
                    Toast.makeText(it.context, "Item Added successfully", Toast.LENGTH_SHORT).show()
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
    fun searchInCartDB(id:String) :Boolean
}