package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.databinding.IndiviewCategoryBinding

class CategoryAdapter(val callback: CategoryAdapterCallback) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    private val item = ArrayList<CategoryModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CategoryModel>) {
        item.clear()
        item.addAll(list)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(private val binding: IndiviewCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryModel) {
            with(binding) {
                imgCategoryIcon.load(item.categoryIconLink) {
                    transformations(CircleCropTransformation())
                }
                tvCategoryName.text = item.categoryName.uppercase()
                constraintLayout.setOnClickListener {
                    callback.onItemClick(item.categoryName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            IndiviewCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size


}

interface CategoryAdapterCallback {
    fun onItemClick(productName: String)
}