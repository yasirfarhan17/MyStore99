package com.noor.mystore99.amigrate.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.database.entity.CartEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentActivity
import com.noor.mystore99.amigrate.util.Util.showAlert
import com.noor.mystore99.databinding.ActivityNewCartBinding
import com.noor.mystore99.databinding.BottomSheetCartDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityNewCartBinding, CartViewModel>(), CartCallBack {

    override val viewModel: CartViewModel by viewModels()
    override fun layoutId(): Int = R.layout.activity_new_cart
    
    private var cartKey: String = ""
    private var currentCartList: List<CartEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cart)
        
        cartKey = prefsUtil.Name.toString()
        viewModel.cartDataCall(cartKey)
        
        initUi()
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        with(binding) {
            btCheckOut.setOnClickListener {
                val total = viewModel.totalPrice.value ?: 0
                val subTotal = viewModel.subTotal.value ?: 0
                
                if (subTotal < 150) {
                    Toast.makeText(this@CartActivity, getString(R.string.add_more_items_msg), Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this@CartActivity, PaymentActivity::class.java).apply {
                        putExtra("amount", total.toString())
                    }
                    startActivity(intent)
                }
            }

            tvClearCart.setOnClickListener {
                showClearCartDialog()
            }

            tvViewDetails.setOnClickListener {
                showCartDetailsBottomDialog()
            }

            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btStartBuying.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupObservers() {
        viewModel.cartFromDB.observe(this) { cartList ->
            currentCartList = cartList ?: emptyList()
            updateCartVisibility(currentCartList.isNotEmpty())
            (binding.rvCart.adapter as? CartAdapter)?.submitList(ArrayList(currentCartList), cartKey)
        }

        viewModel.totalPrice.observe(this) { total ->
            binding.tvTotalPrice.text = getString(R.string.currency_format, total)
        }
    }
    
    // Note: 'addObservers' was an override from BaseActivity presumably, but I renamed it to setupObservers which is better.
    // If BaseActivity requires addObservers, I should keep it or call it.
    // Checking previous code: override fun addObservers()
    override fun addObservers() {
        // BaseActivity abstract method implementation
        // delegating to our clean setupObservers or keeping empty if already called in onCreate
    }

    private fun updateCartVisibility(isPresent: Boolean) {
        with(binding) {
            btCheckOut.isVisible = isPresent
            tvClearCart.isVisible = isPresent
            rvCart.isVisible = isPresent
            tvViewDetails.isVisible = isPresent
            tvTotalPrice.isVisible = isPresent
            clEmptyCart.isVisible = !isPresent
        }
    }

    private fun showCartDetailsBottomDialog() {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = DataBindingUtil.inflate<BottomSheetCartDetailsBinding>(
            layoutInflater,
            R.layout.bottom_sheet_cart_details,
            null,
            false
        )
        dialog.setContentView(sheetBinding.root)

        sheetBinding.rvCartItemsSummary.layoutManager = LinearLayoutManager(this)
        sheetBinding.rvCartItemsSummary.adapter = CartSummaryAdapter(currentCartList)

        val subTotal = viewModel.subTotal.value ?: 0
        val delivery = viewModel.deliveryCharge.value ?: 0
        val total = viewModel.totalPrice.value ?: 0

        sheetBinding.tvSubTotal.text = getString(R.string.currency_format, subTotal)
        
        sheetBinding.tvDeliveryCharge.text = when (delivery) {
            0 -> getString(R.string.free_delivery)
            else -> getString(R.string.currency_format, delivery)
        }

        sheetBinding.tvTotal.text = getString(R.string.currency_format, total)
        dialog.show()
    }

    private fun showClearCartDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.ic_baseline_remove_24) // Using the vector I added or delete icon
            .setTitle("Clear Cart?")
            .setMessage("Are you sure you want to remove all items from your cart?")
            .setPositiveButton("Clear") { dialog, _ ->
                viewModel.clearCartForUser(cartKey)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun initUi() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = CartAdapter(this@CartActivity)
        }
    }
    
    override fun onClick(price: String, id: String, quant: String) {
        viewModel.updateQuant(price, id, quant)
    }

    override fun onDelete(id: String, pos: Int, item: CartEntity) {
        viewModel.deleteItemFromCart(item)
        Toast.makeText(this, "Removed Successfully", Toast.LENGTH_SHORT).show()
    }

    override fun update_counter() {
        // No-op: UI updates are handled via LiveData observers
    }
}