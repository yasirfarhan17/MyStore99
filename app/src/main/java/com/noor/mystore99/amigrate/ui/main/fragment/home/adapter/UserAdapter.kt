package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.amigrate.util.extension.CoilExtension.loadBitmap
import com.noor.mystore99.amigrate.util.extension.Util.decodeToBitmap
import com.noor.mystore99.databinding.CardviewBinding

class UserAdapter(val context: UserFragment) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


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

    inner class UserViewHolder(private val binding: CardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                name.text = item.products_name
                price.text = item.price
                hindiName.text = item.HindiName
                productImg.loadBitmap(item.img!!.decodeToBitmap())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size


}