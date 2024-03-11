package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.google.firebase.database.*
import com.noor.mystore99.R
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter(
    val callback:CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val items = ArrayList<CartEntity>()

    var ref=FirebaseDatabase.getInstance().getReference("Variety")
    var count=0
   lateinit var key:String
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CartEntity>,key:String) {
        items.clear()
        items.addAll(list)
        this.key =key
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapter() {
        items.clear()
        notifyDataSetChanged()
    }


    inner class CartViewHolder(private val binding: IndiviewCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartEntity) {
            with(binding) {
                Log.d("insideCartAdapter",items.size.toString()+" "+item)
                tvName.text = item.products_name
                tvCartQuant.text = item.weight
                tvCartType.text = "₹ "+item.price
                binding.vBt1.setVisibility(View.INVISIBLE)
                binding.vBt2.setVisibility(View.INVISIBLE)
                binding.vBt3.setVisibility(View.INVISIBLE)
                tvCurrentQuant.text=item.quant
                var tot= item.quant?.toInt()?.let { item.price?.toInt()?.times(it) }
                total.text = "₹ "+tot
                    imgCart.load(item.img) {
                        transformations(CircleCropTransformation())
                        placeholder(R.drawable.ic_home_black_24dp)
                    }
                btIncrease.setOnClickListener {
                    item.quant= (item.quant?.toInt()?.plus(1)).toString()
                    item.total= (item.price?.toInt()?.times(item.quant!!.toInt())).toString()
                    callback.onClick(item.total!!,item.products_name, item.quant!!)
                    tvCurrentQuant.text=item.quant
                    total.text="₹ " + item.total
                   // CartActivity.subValue =CartActivity.subValue + item.total!!.toInt()
                    //notifyDataSetChanged()
                }
                btMinus.setOnClickListener {
                    if(item.quant!!.toInt() > 1) {
                        item.quant = (item.quant?.toInt()?.minus(1)).toString()
                        item.total = (item.price?.toInt()?.times(item.quant!!.toInt())).toString()
                        callback.onClick(item.total!!, item.products_name, item.quant!!)
                        tvCurrentQuant.text = item.quant
                        total.text = "₹ " + item.total
                       // CartActivity.subValue =CartActivity.subValue - item.total!!.toInt()
                    }
                    //notifyDataSetChanged()
                }
                imgClear.setOnClickListener {
                    items.removeAt(position)
                    //Log.d("insideCartAdapter", "$position $items")
                    notifyItemRemoved(position)
                    callback.onDelete(item.products_name,position,item)


                }


                ref = FirebaseDatabase.getInstance().getReference("variety")
                    .child(item.products_name)
                ref.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                        val `val` = dataSnapshot.childrenCount.toInt()
                        count = `val`
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                    override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                    override fun onCancelled(databaseError: DatabaseError) {}
                })


                ref = FirebaseDatabase.getInstance().getReference("variety")
                    .child(item.products_name)
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.vBt1.setVisibility(View.VISIBLE)
                        }
                        if (dataSnapshot.child("500gm").exists()) {
                            binding.vBt2.setVisibility(View.VISIBLE)
                        }
                        if (dataSnapshot.child("250gm").exists()) {
                            binding.vBt3.setVisibility(View.VISIBLE)
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })



                binding.vBt1.setOnClickListener(View.OnClickListener {
                    tvCartQuant.text=("Per Kg")
                    if (item.weight == "500gm" || item.weight == "500Gm"
                    ) {
                        tvCartType.text = ("₹ " + item.price!!.toInt() * 2)

                        val str: String = item.price.toString()
                        val str1 = str.substring(2, str.length)
                        item.price= (item.price!!.toInt() * 2).toString()
                        if(item.quant?.toInt()==0){
                            item.quant= (item.quant?.toInt()?.plus(1)).toString()
                            binding.total.setText(
                                "₹ " + (item.price?.toInt()!!).times(1))
                        }
                        else{
                            binding.total.setText(
                                "₹ " + (item.price?.toInt()!!).times(item.quant?.toInt()!!))
                        }
                        Log.d("insideKg",
                            item.quant?.toInt()?.times(item.price?.toInt()!!).toString()+" "+item.quant+" "+item.price

                        )


                    } else if (item.weight == "250gm" || item.weight == "250Gm"
                    ) {
                        tvCartType.text=("₹ " + (item.price?.toInt()?.times(4) ?:1))

                        val str: String = tvCartType.text.toString()
                        val str1 = str.substring(2, str.length)
                        item.price=(str1)
                        if(item.quant?.toInt()==0){
                            item.quant= (item.quant?.toInt()?.plus(1)).toString()
                        }
                        binding.total.setText(
                            "₹ " + (item.quant?.toInt()?.times(item.price?.toInt()!!)))
                    }
                    item.weight=("Per Kg")
                    vBt1.isEnabled = false
                    vBt2.isEnabled = true
                    vBt3.isEnabled = true
                    val str: String = tvCartType.text.toString()
                    val str1 = str.substring(2, str.length)
                    ref = FirebaseDatabase.getInstance().getReference("CartNew").child(key)
                        .child(item.products_name)
                    ref.child("weight").setValue("Per kg")
                    ref.child("price").setValue(item.price)
                    item.weight= ("Per Kg")
                    ref.child("total")
                        .setValue((item.quant?.toInt()?.times(item.price?.toInt()!!)).toString())

                    callback.update_counter()
                    notifyItemChanged(position)
//                    rate = 0
//                    for (i in `val`.indices) {
//                        rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                    }
//                    CartProductList.update_counter(rate.toString())
//                    vBt1.setBackgroundResource(R.drawable.cartbg1)
//                    vBt2.setBackgroundResource(R.drawable.cartbg)
//                    vBt3.setBackgroundResource(R.drawable.cartbg)
                })



                vBt2.setOnClickListener(View.OnClickListener {
                    tvCartQuant.text = "500gm"

                    ref = FirebaseDatabase.getInstance().getReference("variety")
                        .child(item.products_name).child("500gm").child("rate")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val val1 = dataSnapshot.value.toString()
                                tvCartType.text = "₹ $val1"
                                item.price=val1

                                ref = FirebaseDatabase.getInstance().getReference("CartNew").child(key)
                                    .child(item.products_name)
                                ref.child("weight").setValue("500gm")
                                ref.child("price").setValue(val1)
                                ref.child("total")
                                    .setValue((item.quant?.toInt()?.times(val1.toInt())).toString())
                                binding.total.setText(
                                    "₹ " + ((item.quant?.toInt()?.times(val1.toInt())).toString()))
                                callback.update_counter()
                                notifyItemChanged(position)
//                                rate = 0
//                                for (i in `val`.indices) {
//                                    rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                                }
//                                CartProductList.update_counter(rate.toString())
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    vBt1.isEnabled = true
                    vBt2.isEnabled = false
                    vBt3.isEnabled = true
//                    vBt2.setBackgroundResource(R.drawable.cartbg1)
//                    vBt1.setBackgroundResource(R.drawable.cartbg)
//                    vBt3.setBackgroundResource(R.drawable.cartbg)


                })

                vBt3.setOnClickListener(View.OnClickListener {
                    tvCartQuant.text = "250gm"
                    ref = FirebaseDatabase.getInstance().getReference("variety")
                        .child(item.products_name).child("250gm").child("rate")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val val1 = dataSnapshot.value.toString()
                                tvCartType.text = "₹ $val1"
                                item.price=val1
                                ref = FirebaseDatabase.getInstance().getReference("CartNew").child(key)
                                    .child(item.products_name)
                                ref.child("weight").setValue("250gm")
                                ref.child("price").setValue(val1)
                                ref.child("total")
                                    .setValue((item.quant?.toInt()?.times(val1.toInt())).toString())
                                binding.total.setText(
                                    "₹ " + ((item.quant?.toInt()?.times(val1.toInt())).toString()))
                                callback.update_counter()
                                notifyItemChanged(position)
//                                rate = 0
//                                for (i in `val`.indices) {
//                                    rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                                }
//                                CartProductList.update_counter(rate.toString())
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    vBt1.isEnabled = true
                    vBt2.isEnabled = true
                    vBt3.isEnabled = false

                    //holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
//                    vBt3.setBackgroundResource(R.drawable.cartbg1)
//                    vBt2.setBackgroundResource(R.drawable.cartbg)
//                    vBt1.setBackgroundResource(R.drawable.cartbg)

                })

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val binding =
            IndiviewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(items[position])

    }

    override fun getItemCount(): Int = items.size
}

interface cartCallBack{
    fun onClick(price:String,id:String,quant:String)
    fun onDelete(id: String, pos: Int, item: CartEntity)
}