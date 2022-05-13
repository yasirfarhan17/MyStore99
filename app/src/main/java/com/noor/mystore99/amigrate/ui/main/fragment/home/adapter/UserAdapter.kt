package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.IndiviewProductsBinding

class UserAdapter(
    val callBack: UserAdapterCallBack
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val items = ArrayList<ProductEntity>()
    private val itemFilter = ArrayList<ProductEntity>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ProductEntity>) {
        items.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun updateItem(item: ProductEntity, position: Int) {
        items.removeAt(position)
        items.add(position, item)
        notifyItemChanged(position)
    }


    inner class UserViewHolder(private val binding: IndiviewProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                if (item.count == 0) {
                    clAddDec.setVisible(false)
                    btAddToCart.setVisible(true)
                } else {
                    clAddDec.setVisible(true)
                    btAddToCart.setVisible(false)
                }
                tvCurrentCount.text = item.count.toString()
                tvName.text = item.products_name
                tvMrp.text = item.price
                tvHindiName.text = item.HindiName
                imgProductImage.load(item.img?.decodeToBitmap()) {
                    transformations(CircleCropTransformation())
                }
                btAddToCart.setOnClickListener {
                    callBack.onAddToCartClick(item, adapterPosition)
                }
                btIncrease.setOnClickListener {
                    callBack.onIncreaseItemClick(item, adapterPosition)
                }
                btDecrease.setOnClickListener {
                    callBack.onDecreaseItemClick(item, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            IndiviewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}

interface UserAdapterCallBack {
    fun onAddToCartClick(item: ProductEntity, position: Int)
    fun onIncreaseItemClick(item: ProductEntity, position: Int)
    fun onDecreaseItemClick(item: ProductEntity, position: Int)

}