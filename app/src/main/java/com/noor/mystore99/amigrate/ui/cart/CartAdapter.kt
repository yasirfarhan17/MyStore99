package com.noor.mystore99.amigrate.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.networkmodule.database.entity.CartEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.R
import com.noor.mystore99.databinding.IndiviewCartBinding

class CartAdapter(
    private val callback: CartCallBack
) : ListAdapter<CartEntity, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    private var cartKey: String = ""

    fun submitList(list: List<CartEntity>?, key: String) {
        cartKey = key
        super.submitList(list?.toList()) // Submit a copy to trigger DiffUtil if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = IndiviewCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: IndiviewCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartEntity) {
            with(binding) {
                tvName.text = item.products_name
                tvCartQuant.text = item.weight
                tvCartType.text = "₹ ${item.price}"
                tvCurrentQuant.text = item.quant
                
                val totalPrice = (item.quant?.toIntOrNull() ?: 0) * (item.price?.toIntOrNull() ?: 0)
                total.text = "₹ $totalPrice"

                imgCart.load(item.img) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_local_mall_black_24dp)
                    error(R.drawable.ic_local_mall_black_24dp)
                }

                updateSelectionState(item)
                setupClickListeners(item)
                checkVarietyAvailability(item)
            }
        }

        private fun IndiviewCartBinding.updateSelectionState(item: CartEntity) {
            val weight = item.weight ?: ""
            // Prevent infinite loop if listener triggers update? 
            // MaterialButtonToggleGroup listener might trigger. We are using onClickListeners below so it's fine.
            // But checking programmatically might verify visual state.
            
            if (weight.contains("500", ignoreCase = true)) {
                toggleWeightGroup.check(R.id.btn_variant_500gm)
            } else if (weight.contains("250", ignoreCase = true)) {
                toggleWeightGroup.check(R.id.btn_variant_250gm)
            } else {
                toggleWeightGroup.check(R.id.btn_variant_1kg)
            }
        }

        private fun IndiviewCartBinding.setupClickListeners(item: CartEntity) {
            btIncrease.setOnClickListener {
                val currentQuant = item.quant?.toIntOrNull() ?: 0
                val newQuant = currentQuant + 1
                updateItemQuantity(item, newQuant)
            }

            btMinus.setOnClickListener {
                val currentQuant = item.quant?.toIntOrNull() ?: 0
                if (currentQuant > 1) {
                    val newQuant = currentQuant - 1
                    updateItemQuantity(item, newQuant)
                }
            }

            imgClear.setOnClickListener {
                callback.onDelete(item.products_name, adapterPosition, item)
            }

            btnVariant1kg.setOnClickListener { updateWeight(item, "1kg", 2) }
            btnVariant500gm.setOnClickListener { updateWeightFromFirebase(item, "500gm") }
            btnVariant250gm.setOnClickListener { updateWeightFromFirebase(item, "250gm") }
        }

        private fun updateItemQuantity(item: CartEntity, newQuant: Int) {
            item.quant = newQuant.toString()
            val price = item.price?.toIntOrNull() ?: 0
            item.total = (price * newQuant).toString()
            
            // Optimistic UI update
            binding.tvCurrentQuant.text = item.quant
            binding.total.text = "₹ ${item.total}"

            callback.onClick(item.total!!, item.products_name, item.quant!!)
        }

        private fun updateWeight(item: CartEntity, weightLabel: String, priceMultiplier: Int) {
            val currentPrice = item.price?.toIntOrNull() ?: 0
            var newPrice = currentPrice

            // Logic to restore base "Per Kg" price from fractional weights, assuming we are moving TO 1kg
            // If currently 500gm, base is *2. If 250gm, base is *4.
            if (item.weight.equals("500gm", ignoreCase = true)) {
                newPrice = currentPrice * 2
            } else if (item.weight.equals("250gm", ignoreCase = true)) {
                newPrice = currentPrice * 4
            }
            // If already 1kg/Per Kg, price is likely already correct.
            
            updateCartItemOnFirebase(item, "Per Kg", newPrice.toString())
        }
        
        private fun updateWeightFromFirebase(item: CartEntity, weightNode: String) {
             val ref = FirebaseDatabase.getInstance().getReference("variety")
                .child(item.products_name).child(weightNode).child("rate")
            
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val newPrice = snapshot.value.toString()
                        updateCartItemOnFirebase(item, weightNode, newPrice)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        private fun updateCartItemOnFirebase(item: CartEntity, weight: String, price: String) {
            val ref = FirebaseDatabase.getInstance().getReference("CartNew").child(cartKey).child(item.products_name)
            val quant = item.quant?.toIntOrNull() ?: 1
            val total = quant * (price.toIntOrNull() ?: 0)

            ref.apply {
                child("weight").setValue(weight)
                child("price").setValue(price)
                child("total").setValue(total.toString())
            }
            
            // Update local item state to reflect immediately
            item.weight = weight
            item.price = price
            item.total = total.toString()
            notifyItemChanged(adapterPosition)
        }

        private fun checkVarietyAvailability(item: CartEntity) {
             val ref = FirebaseDatabase.getInstance().getReference("variety").child(item.products_name)
             ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Only show controls if there are variants
                    val hasVariants = snapshot.exists()
                    binding.toggleWeightGroup.isVisible = hasVariants
                    binding.tvPackSizeLabel.isVisible = hasVariants
                    
                    // Show specific buttons if they exist in DB
                    // Note: 'btnVariant1kg' is assumed to be the base, so we show it if variants exist generally?
                    // Or if "1kg" node exists? The logic before was `binding.vBt1.isVisible = snapshot.exists()`.
                    binding.btnVariant1kg.isVisible = true 
                    binding.btnVariant500gm.isVisible = snapshot.child("500gm").exists()
                    binding.btnVariant250gm.isVisible = snapshot.child("250gm").exists()
                }
                override fun onCancelled(error: DatabaseError) {}
             })
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartEntity>() {
    override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem.products_name == newItem.products_name 
    }

    override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
        return oldItem == newItem
    }
}

interface CartCallBack {
    fun onClick(price: String, id: String, quant: String)
    fun onDelete(id: String, pos: Int, item: CartEntity)
    fun update_counter() // Kept for compatibility if used, though strict refactor would remove it
}