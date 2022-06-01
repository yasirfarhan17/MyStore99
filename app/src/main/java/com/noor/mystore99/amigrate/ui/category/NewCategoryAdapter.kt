package com.noor.mystore99.amigrate.ui.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewProductsBinding
import javax.inject.Inject

class NewCategoryAdapter(
    val callBack: CategoryActivity
) : RecyclerView.Adapter<NewCategoryAdapter.NewCategoryViewHolder>() {
    private val item = ArrayList<ProductEntity>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ProductEntity>) {
        item.clear()
        item.addAll(list)
        notifyDataSetChanged()
    }

    inner class NewCategoryViewHolder(private val binding: IndiviewProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvMrp.text = item.price
                tvHindiName.text = item.HindiName
                imgProductImage.load(item.img?.decodeToBitmap()) {
                    transformations(CircleCropTransformation())
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
                    btAddToCart.visibility= View.GONE
                    btMinus.visibility= View.VISIBLE
                    btIncrease.visibility= View.VISIBLE
                    tvCurrentQuant.visibility= View.VISIBLE
                    Toast.makeText(it.context, "Item Added successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCategoryViewHolder {
        val binding =
            IndiviewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCategoryViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size

}

interface CategoryCallBack {
    fun onItemClick(item: CartEntity)

}