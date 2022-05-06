package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.amigrate.util.extension.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewProductsBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val item = ArrayList<ProductEntity>()
    private val itemFilter = ArrayList<ProductEntity>()


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ProductEntity>) {
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
                item.img!!.decodeToBitmap(500)?.let {
                    imgProductImage.load(it) {
                        transformations(CircleCropTransformation())
                    }
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
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size


}