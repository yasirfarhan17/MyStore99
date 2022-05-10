package com.noor.mystore99.amigrate.ui.category

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.amigrate.ui.category.NewCategoryAdapter.*
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.databinding.IndiviewProductsBinding

class NewCategoryAdapter(
    val callBack:CategoryActivity
) :RecyclerView.Adapter<NewCategoryAdapter.NewCategoryViewHolder>() {

    private val item = ArrayList<ProductEntity>()
    private val itemFilter = ArrayList<ProductEntity>()

    private lateinit var viewModel: UserViewModel

    fun submitList(list:ArrayList<ProductEntity>){
        item.clear()
        itemFilter.clear()
        item.addAll(list)
        itemFilter.addAll(list)
    }
    inner class NewCategoryViewHolder(private val  binding:IndiviewProductsBinding)
        :RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvMrp.text = item.price
                tvHindiName.text = item.HindiName
                imgProductImage.load(item.img?.decodeToBitmap()) {
                    transformations(CircleCropTransformation())
                }

                val cartEntity =
                    CartEntity(item.products_name, item.price, item.img, item.quant, item.price)
                btAddToCart.setOnClickListener {
                    //viewModel.insertToCartDb(cartEntity)
                    callBack.onItemClick(cartEntity)
                    android.widget.Toast.makeText(
                        it.context,
                        "Item Added successfully",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("insideAdapter",item.products_name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCategoryViewHolder {
        val binding=IndiviewProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return NewCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewCategoryViewHolder, position: Int) {
       holder.bind(item[position])
    }

    override fun getItemCount(): Int =item.size

}
interface CallBackCategory{
    fun onItemClick(cartEntity: CartEntity)

}