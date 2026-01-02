package com.noor.mystore99.amigrate.ui.payment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.network.Resource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
import com.noor.mystore99.amigrate.ui.dashboard.account.address.AddressActivity
import com.noor.mystore99.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    private var cartList = ArrayList<CartModel>()
    private lateinit var userId: String
    private lateinit var amount: String
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)

        userId = prefsUtil.Name.toString()
        amount = intent.getStringExtra("amount") ?: "0"

        initUi()
        setupListeners()
        setupObservers()

        viewModel.getUserDet(userId)
        cartViewModel.cartDataCall(userId)
    }

    override fun layoutId(): Int = R.layout.activity_payment

    private fun initUi() {
        binding.etDate.setOnClickListener { showDatePicker() }
        // setEndIconOnClickListener is not available on TextInputEditText, avoiding it or using TIL if bound
        binding.tilDate.setEndIconOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year1, monthOfYear, dayOfMonth ->
            val dateStr = "$dayOfMonth/${monthOfYear + 1}/$year1"
            binding.etDate.setText(dateStr)
        }, year, month, day)
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    private fun setupListeners() {
        with(binding) {
            imgBack.setOnClickListener { onBackPressed() }

            tvChangeAddress.setOnClickListener {
                val intent = Intent(this@PaymentActivity, AddressActivity::class.java)
                startActivity(intent)
            }

            btCheckOut.setOnClickListener {
                handleCheckout()
            }
        }
    }

    private fun handleCheckout() {
        // Validation
        val date = binding.etDate.text.toString()
        val pincode = binding.etPincode.text.toString()
        val address = binding.tvAddress.text.toString()

        if (date.isBlank()) {
            Toast.makeText(this, "Please choose a delivery date", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (address.isBlank() || address.contains("null") || address.length < 5) {
            Toast.makeText(this, "Valid shipping address is required", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (pincode.isBlank() || pincode.length < 4) {
             Toast.makeText(this, "Valid pincode is required", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (amount == "0" || amount == "null") {
             Toast.makeText(this, "Invalid Cart Amount", Toast.LENGTH_SHORT).show()
             return
        }

        // Proceed to Verify Pincode
        viewModel.verifyPincode(pincode)
    }

    private fun setupObservers() {
        // User Details
        viewModel.userDetail.observe(this) { user ->
            if (user != null) {
                val addr = user.address ?: ""
                val pin = user.pincode ?: ""
                
                binding.tvName.text = user.name?.takeIf { it.isNotBlank() } ?: "User"
                
                if (addr.isNotBlank()) {
                     binding.tvAddress.text = "$addr\nPincode - $pin"
                } else {
                     binding.tvAddress.text = "No address found. Please add one."
                }
                
                binding.tvPhoneNumber.text = userId
                binding.etPincode.setText(pin)
                userName = user.name ?: ""
            }
        }

        // Cart Data
        cartViewModel.cartFromDB.observe(this) { entities ->
            cartList = entities.mapTo(ArrayList()) {
                CartModel(it.products_name, it.price, it.img, it.weight, it.quant, it.total)
            }
        }

        // Pincode Status
        viewModel.pincodeStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                     binding.btCheckOut.isEnabled = false
                     binding.btCheckOut.text = "Verifying..."
                }
                is Resource.Success -> {
                    // Pincode Verified -> Place Order (COD)
                    binding.btCheckOut.text = "Processing Order..."
                    
                    val date = binding.etDate.text.toString()
                    val pincode = binding.etPincode.text.toString()
                    val address = binding.tvAddress.text.toString()

                    viewModel.placeOrder(
                        cartList = cartList,
                        amount = amount,
                        date = date,
                        address = address,
                        pincode = pincode,
                        paymentMode = "cod",
                        userId = userId,
                        userName = userName
                    )
                }
                is Resource.Error -> {
                    binding.btCheckOut.isEnabled = true
                    binding.btCheckOut.text = "Place Order"
                    Toast.makeText(this, resource.message ?: "Service unavailable at this pincode", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Order Status
        viewModel.orderStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Already handled in flow usually, but double safety
                    binding.btCheckOut.isEnabled = false
                }
                is Resource.Success -> {
                    val intent = Intent(this, confirmOrder::class.java)
                    intent.putExtra("pay", "Cash on delivery")
                    intent.putExtra("amount", amount)
                    intent.putExtra("combo", resource.data)
                    intent.putExtra("pincode", binding.etPincode.text.toString())
                    
                    cartViewModel.clearCart()
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    binding.btCheckOut.isEnabled = true
                    binding.btCheckOut.text = "Place Order"
                    Toast.makeText(this, "Failed: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun addObservers() {}
}