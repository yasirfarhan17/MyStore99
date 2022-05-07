package com.noor.mystore99.amigrate.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentActivity
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.Util.showAlert
import com.noor.mystore99.databinding.ActivityNewCartBinding
import com.noor.mystore99.databinding.BottomSheetCartDetailsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityNewCartBinding, CartViewModel>() {

    override val viewModel: CartViewModel by viewModels()
    override fun layoutId(): Int = R.layout.activity_new_cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_cart)
        initUi()
        addListener()
    }

    private fun addListener() {
        with(binding) {
            btCheckOut.setOnClickListener {
                startActivity(Intent(this@CartActivity, PaymentActivity::class.java))
            }
            imgClearCart.setOnClickListener {
                showAlert(
                    this@CartActivity,
                    getString(R.string.txt_clear_cart_question),
                    getString(R.string.all_the_item_in_cart_will_be_cleared)
                ) {
                    viewModel.clearCart()
                    itemsPresentInCart(false)
                    (binding.rvCart.adapter as CartAdapter).clearAdapter()

                }
            }
            tvViewDetails.setOnClickListener {
                showCartDetailsBottomDialog()
            }
            imgBack.setOnClickListener {
                onBackPressed()
            }
            btStartBuying.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun showCartDetailsBottomDialog() {
        val cartBottomSheetDialog = BottomSheetDialog(this)
        val bindingSheet = DataBindingUtil.inflate<BottomSheetCartDetailsBinding>(
            layoutInflater,
            R.layout.bottom_sheet_cart_details,
            null,
            false
        )
        cartBottomSheetDialog.setContentView(bindingSheet.root)
        cartBottomSheetDialog.create()
        cartBottomSheetDialog.show()
    }

    private fun initUi() {
        with(binding) {
            rvCart.layoutManager = LinearLayoutManager(this@CartActivity)
            rvCart.adapter = CartAdapter()
        }


    }

    override fun addObservers() {
        viewModel.cartFromDB.observe(this) { cartList ->
            if (cartList.isNullOrEmpty()) {
                itemsPresentInCart(false)
                return@observe
            }
            itemsPresentInCart(true)
            (binding.rvCart.adapter as CartAdapter).submitList(cartList)
            Log.d("cartcheck", "" + cartList)
        }
    }

    private fun itemsPresentInCart(isPresent: Boolean) {
        binding.btCheckOut.setVisible(isPresent)
        binding.imgClearCart.setVisible(isPresent)
        binding.rvCart.setVisible(isPresent)
        binding.tvViewDetails.setVisible(isPresent)
        binding.clEmptyCart.setVisible(isPresent.not())
        binding.tvTotalPrice.setVisible(isPresent)

    }
}