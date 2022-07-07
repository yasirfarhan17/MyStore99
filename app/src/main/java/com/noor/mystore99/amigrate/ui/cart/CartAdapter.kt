package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.google.firebase.database.*
import com.noor.mystore99.CartProductList
import com.noor.mystore99.R
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter(
    val callback:CartActivity
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val items = ArrayList<CartEntity>()

    var ref=FirebaseDatabase.getInstance().getReference("Variety")
    var count=0

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CartEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAdapter() {
        items.clear()
        notifyDataSetChanged()
    }


    inner class CartViewHolder(private val binding: IndiviewCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvCartQuant.text = item.weight
                tvCartType.text = "₹ "+item.price
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
                    callback.onDelete(item.products_name,position,item)
                    notifyItemRemoved(position)
                    items.removeAt(position)
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
                    tvCartQuant.text=("Weight:Per Kg")
                    if (item.weight == "500gm" || item.weight == "500Gm"
                    ) {
                        tvCartType.text = ("₹ " + item.price!!.toInt() * 2)
                        val str: String = item.price.toString()
                        val str1 = str.substring(2, str.length)
                        item.price=(str1)
                    } else if (item.weight == "250gm" || item.weight == "250Gm"
                    ) {
                        tvCartType.text=("₹ " + (item.price?.toInt()?.times(4) ?:1))
                        val str: String = tvCartType.text.toString()
                        val str1 = str.substring(2, str.length)
                        item.price=(str1)
                    }
                    item.weight=("Per Kg")
                    vBt1.isEnabled = false
                    vBt2.isEnabled = true
                    vBt3.isEnabled = true
                    val str: String = tvCartType.text.toString()
                    val str1 = str.substring(2, str.length)
//                    ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                        .child(item.products_name)
//                    ref.child("price").setValue(str1)
//                    ref.child("weight").setValue("Per kg")
//                    item.weight= ("Per Kg")
//                    ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                        .child(item.products_name)
//                    ref.child("total")
//                        .setValue(`val`.get(position) * item.price)
//                    holder.t2.setText(
//                        "₹ " + (`val`.get(position) * product1.get(position).getPrice()
//                            .toInt()).toString()
//                    )
//                    rate = 0
//                    for (i in `val`.indices) {
//                        rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                    }
//                    CartProductList.update_counter(rate.toString())
                    vBt1.setBackgroundResource(R.drawable.cartbg1)
                    vBt2.setBackgroundResource(R.drawable.cartbg)
                    vBt3.setBackgroundResource(R.drawable.cartbg)
                })



                vBt2.setOnClickListener(View.OnClickListener {
                    tvCartQuant.setText("Weight:500gm")
                    vBt1.setEnabled(true)
                    vBt2.setEnabled(false)
                    vBt3.setEnabled(true)
                    ref = FirebaseDatabase.getInstance().getReference("variety")
                        .child(item.products_name).child("500gm").child("rate")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val val1 = dataSnapshot.value.toString()
                                tvCartType.setText("₹ $val1")
//                                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                                    .child(item.products_name)
//                                ref.child("price").setValue(val1)
//                                ref.child("weight").setValue("500gm")
//                                item.weight=("500gm")
//                                item.price=(val1)
//                                holder.t2.setText(
//                                    "₹ " + (`val`.get(position) * product1.get(
//                                        position
//                                    ).getPrice().toInt()).toString()
//                                )
//                                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                                    .child(item.products_name)
//                                ref.child("total").setValue(
//                                    `val`.get(position) * item.price
//                                )
//                                rate = 0
//                                for (i in `val`.indices) {
//                                    rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                                }
//                                CartProductList.update_counter(rate.toString())
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    vBt2.setBackgroundResource(R.drawable.cartbg1)
                    vBt1.setBackgroundResource(R.drawable.cartbg)
                    vBt3.setBackgroundResource(R.drawable.cartbg)
                })

                vBt3.setOnClickListener(View.OnClickListener {
                    tvCartQuant.setText("Weight:250gm")
                    ref = FirebaseDatabase.getInstance().getReference("variety")
                        .child(item.products_name).child("250gm").child("rate")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val val1 = dataSnapshot.value.toString()
                                tvCartType.setText("₹ $val1")
//                                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                                    .child(item.products_name)
//                                ref.child("price").setValue(val1)
//                                ref.child("weight").setValue("250gm")
//                                item.weight=("250gm")
//                                item.price=(val1)
//                                holder.t2.setText(
//                                    "₹ " + (`val`.get(position) * item.price).toString()
//                                )
//                                ref = FirebaseDatabase.getInstance().getReference("Cart").child(key)
//                                    .child(item.products_name)
//                                ref.child("total").setValue(
//                                    `val`.get(position) * item.price.toInt()
//                                )
//                                rate = 0
//                                for (i in `val`.indices) {
//                                    rate = rate + `val`.get(i) * product1.get(i).getPrice().toInt()
//                                }
//                                CartProductList.update_counter(rate.toString())
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    vBt1.setEnabled(true)
                    vBt2.setEnabled(true)
                    vBt3.setEnabled(false)

                    //holder.t2.setText("₹ " + String.valueOf(val.get(position) * (Integer.parseInt(product1.get(position).getPrice()))));
                    vBt3.setBackgroundResource(R.drawable.cartbg1)
                    vBt2.setBackgroundResource(R.drawable.cartbg)
                    vBt1.setBackgroundResource(R.drawable.cartbg)
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