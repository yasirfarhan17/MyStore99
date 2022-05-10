package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.databinding.IndiviewCategoryBinding

class CategoryAdapter(val callBAck:UserFragment) :
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
                imgCategoryIcon.load(item.categoryIconLink.decodeToBitmap(60)){
                    transformations(CircleCropTransformation())
                }
                tvCategoryName.text = item.categoryName.uppercase()
                imgCategoryIcon.invalidate()
                constraintLayout.setOnClickListener {
                    callBAck.onItemClick(item.categoryName,it)
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
interface CallBackCategoryAdapter{
    fun onItemClick(productName:String,view:View)

}