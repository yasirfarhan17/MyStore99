package com.noor.mystore99.amigrate.ui.main.fragment.home.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.databinding.IndiviewNewProductsBinding
import java.util.*

class UserAdapter(
    val callBack: UserFragment
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private val item = ArrayList<ProductEntity>()
    private val itemCart = ArrayList<CartEntity>()
    private val itemFilter = ArrayList<ProductEntity>()
    var ref=FirebaseDatabase.getInstance().reference



    @SuppressLint("NotifyDataSetChanged")
    fun submitList(cartItem:ArrayList<CartEntity>) {

        itemCart.clear()
        itemCart.addAll(cartItem)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitListNeww(list: ArrayList<ProductEntity>) {
        item.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        item.addAll(list)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitListNew(list: ArrayList<ProductEntity>) {
        item.clear()
        itemFilter.clear()
        itemFilter.addAll(list)
        item.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapter() {
        itemCart.clear()
        notifyDataSetChanged()
    }


    inner class UserViewHolder(private val binding: IndiviewNewProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductEntity) {
            with(binding) {
                name.text = "${item.products_name}\n (${item.HindiName})"
                price.text = "₹ " + item.price
                quantBox.text = item.quant
                productImg.load(item.img) {
                    placeholder(R.drawable.ic_local_mall_black_24dp)
                    error(R.drawable.ic_local_mall_black_24dp)
                }
                if (item.stock.equals("no")) {
                    addBtn.visibility = View.GONE
                    stock.visibility = View.VISIBLE
                    btMinus.visibility = View.GONE
                    btIncrease.visibility = View.GONE
                    tvCurrentQuant.visibility = View.GONE
                } else {
                    addBtn.visibility = View.VISIBLE
                    stock.visibility = View.GONE
                    btMinus.visibility = View.GONE
                    btIncrease.visibility = View.GONE
                    tvCurrentQuant.visibility = View.GONE
                }

                ref = FirebaseDatabase.getInstance().getReference("Add Mrp")
                    .child(item.products_name)

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            binding.mrp.setVisibility(View.VISIBLE)
                            binding.textMrp.setVisibility(View.VISIBLE)
                            binding.mrp.setText("₹ " + snapshot.value.toString())
                        } else {
                            binding.mrp.setVisibility(View.INVISIBLE)
                            binding.textMrp.setVisibility(View.INVISIBLE)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })


//                    itemCart?.forEach{
//                        if(it.products_name.equals(item.products_name))
//                        //Log.d("insideItemcart",""+it.products_name)
//                        addBtn.visibility=View.GONE
//                        btMinus.visibility=View.VISIBLE
//                        btIncrease.visibility=View.VISIBLE
//                        tvCurrentQuant.visibility=View.VISIBLE
//                        tvCurrentQuant.text=item.count
//                    }

                btIncrease.setOnClickListener {
                    item.count = (tvCurrentQuant.text.toString()).toInt().plus(1).toString()
                    val total = item.price?.toInt()?.times(item.count!!.toInt())
                    callBack.onClick(total.toString(), item.products_name, item.count!!)
                    tvCurrentQuant.text = item.count
                }
                btMinus.setOnClickListener {
                    if (item.count!!.toInt() > 1) {
                        item.count = (tvCurrentQuant.text.toString().toInt().minus(1)).toString()
                        val total = item.price?.toInt()?.times(item.count!!.toInt())
                        callBack.onClick(total.toString(), item.products_name, item.count!!)

                    }
                    //notifyDataSetChanged()
                }

                Log.d("insideAdapter", " ${item.img}")


                val cartEntity = CartEntity(
                    item.products_name,
                    item.price,
                    item.img,
                    item.quant,
                    "1",
                    item.price
                )

                addBtn.text = "ADD"
                addBtn.isEnabled = true
                addBtn.alpha = 1.0f
                addBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(binding.root.context.getColor(R.color.btnprimary)))
                
                addBtn.setOnClickListener {
                    callBack.onItemClick(cartEntity)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val binding =
            IndiviewNewProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(itemFilter[position])
    }

    override fun getItemCount(): Int = itemFilter.size




}

interface UserAdapterCallBack{
    fun onItemClick(cartEntity: CartEntity)
    fun onClick(price:String,id:String,quant:String)
    fun searchInCartDB(id:String)
}