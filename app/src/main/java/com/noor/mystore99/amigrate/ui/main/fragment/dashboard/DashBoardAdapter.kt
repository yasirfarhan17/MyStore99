package com.noor.mystore99.amigrate.ui.main.fragment.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.DashBoardModel
import com.example.networkmodule.util.Util.decodeToBitmap
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapter
import com.noor.mystore99.amigrate.ui.main.fragment.home.adapter.CategoryAdapterCallback
import com.noor.mystore99.databinding.IndiviewCategoryBinding
import com.noor.mystore99.databinding.IndiviewDashboardBinding

class DashBoardAdapter(
    val callBack: DashboardFragment
)  :
    RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder>() {




    private val item = ArrayList<DashBoardModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<DashBoardModel>) {
        item.clear()
        item.addAll(list)
        notifyDataSetChanged()
    }

    inner class DashBoardViewHolder(private val binding: IndiviewDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashBoardModel) {
            with(binding) {
                imgDash.load(item.Img) {
                    transformations(CircleCropTransformation())
                }
                tvDash.text = item.name?.uppercase()
//                constraintLayout.setOnClickListener {
//                    //callback.onItemClick(item.categoryName)
//                }
                tvDash.setOnClickListener {
                    callBack.onItemClick(item.name.toString())
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val binding =
            IndiviewDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashBoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashBoardAdapter.DashBoardViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size


}

interface DashBoardCallBack {
    fun onItemClick(productName: String)
}
