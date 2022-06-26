package com.noor.mystore99.amigrate.ui.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CartModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
import com.noor.mystore99.amigrate.ui.checkout.CheckoutActivity
import com.example.networkmodule.model.checkOutModel
import com.noor.mystore99.amigrate.ui.dashboard.account.address.Address
import com.noor.mystore99.amigrate.ui.upi.NewUPIPay
import com.noor.mystore99.databinding.ActivityPaymentBinding
import com.noor.mystore99.setDate
import com.noor.mystore99.upiPay
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModels()
     val viewModelCart: CartViewModel by viewModels()
    var cashFlag:Boolean=false
    var upiFlag:Boolean=false
    lateinit var combo:String
    var list =ArrayList<CartModel>()
    lateinit var  key:String
    lateinit var amount:String
    lateinit var address:String
    lateinit var ref : DatabaseReference
    lateinit var ref1 : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        key=prefsUtil.Name.toString()
        amount= intent.getStringExtra("amount").toString()
        val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(
            Date()
        )
        combo = currentDate1 + currentTime1
        Log.d("checkamt", ""+ amount)
        ref= FirebaseDatabase.getInstance().getReference("UserNew").child(key)
        ref1= FirebaseDatabase.getInstance().getReference("orderNew").child(key)
        viewModel.getUserDet(key)
        initUi()
    }



    @SuppressLint("SetTextI18n")
    private fun initUi() {
        with(binding) {
            setDate(this@PaymentActivity, binding.etDate.id)
            viewModel.userDetail.observe(this@PaymentActivity){
                tvAddress.text=it.address.toString()+" Pincode:-"+it.pincode.toString()
                address=tvAddress.text.toString()
                tvPhoneNumber.text=key
                etPincode.setText(it.pincode.toString())

            }
            rbCash.setOnClickListener {
                cashFlag=true
                upiFlag=false
            }
            rbUpi.setOnClickListener {
                cashFlag=false
                upiFlag=true
            }
            btChangePin.setOnClickListener {
                etPincode.setFocusableInTouchMode(true)
                etPincode.setFocusable(true)
            }

            changeAddress.setOnClickListener {
                startActivity(Intent(this@PaymentActivity, Address::class.java))
            }

            btCheckOut.setOnClickListener {
                if(cashFlag){
                    val intent = Intent(this@PaymentActivity, CheckoutActivity::class.java)
                    setValueToFirebase()
                    intent.putExtra("pay","Cash on delivery")
                    intent.putExtra("amount",amount)
                    intent.putExtra("combo",combo)
                    viewModelCart.clearCart()
                    startActivity(intent)
                }
                else if(upiFlag){
//                    val intent = Intent(this@PaymentActivity, CheckoutActivity::class.java)
//                    setValueToFirebase()
//                    intent.putExtra("pay","UPI")
//                    intent.putExtra("amount",amount)
//                    intent.putExtra("combo",combo)
//                    viewModelCart.clearCart()
//                    startActivity(intent)
//                    //viewModelCart.clearCart()
//                    startActivity(intent)
                    startActivity(Intent(this@PaymentActivity,NewUPIPay::class.java))
                }
                else if (binding.etDate.text.isNullOrBlank()){
                    Toast.makeText(this@PaymentActivity,"Please select delivery date",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@PaymentActivity,"Please select the payment option",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_payment

    override fun addObservers() {
        viewModelCart.cartFromDB.observe(this){
            list = it.map{itt ->
                CartModel(itt.products_name,itt.price,itt.img,itt.weight,itt.quant,itt.total)
            } as ArrayList<CartModel>
        }
    }

    private fun setValueToFirebase(){
        val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(
            Date()
        )
        combo = currentDate1 + currentTime1
        val model: checkOutModel
        if(upiFlag){
            model= checkOutModel(list,combo,binding.etDate.text.toString(),currentTime1,address,amount,"upi",key)
        }
        else{
            model= checkOutModel(list,combo,binding.etDate.text.toString(),currentTime1,address,amount,"cod",key)
        }
        ref1.child(combo).setValue(model)

    }
}