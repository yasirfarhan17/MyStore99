package com.noor.mystore99.amigrate.ui.checkout

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.checkOutModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.qrcode.encoder.QRCode
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
import com.noor.mystore99.databinding.ActivityCheckOutBinding
import com.noor.mystore99.databinding.BottomCheckoutDialogeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CheckoutActivity : BaseActivity<ActivityCheckOutBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by viewModels()
     val viewModelcart: CartViewModel by viewModels()
    lateinit var bindingSheet : BottomCheckoutDialogeBinding
    lateinit var localCheckOut:checkOutModel
    var counter=0

    override fun layoutId(): Int = R.layout.activity_check_out
    lateinit var amount:String
    lateinit var paymentMode:String
    lateinit var key:String
    lateinit var id:String
     var  va= ArrayList<String>()
    var localData=ArrayList<CartModel>()
    var qrgEncoder: QRGEncoder? = null
    var bitmap: Bitmap? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_check_out)
        amount= intent.getStringExtra("amount").toString()
        paymentMode= intent.getStringExtra("pay").toString()
        key= prefsUtil.Name.toString()
        id= intent.getStringExtra("combo").toString()
        viewModel.getOrder(key,id)
        addObservers()
        getInit()
        QRCode()
    }




    fun getInit(){

        with(binding){

            rvCart.layoutManager = LinearLayoutManager(this@CheckoutActivity)
            rvCart.adapter = CheckoutAdapter()
            tvViewDetails.setOnClickListener {
                showCartDetailsBottomDialog()
            }
            tvTotalPrice.text=amount

        }
    }
    private fun showCartDetailsBottomDialog() {
        val cartBottomSheetDialog = BottomSheetDialog(this)
        bindingSheet = DataBindingUtil.inflate<BottomCheckoutDialogeBinding>(
            layoutInflater,
            R.layout.bottom_checkout_dialoge,
            null,
            false
        )
        cartBottomSheetDialog.setContentView(bindingSheet.root)
        bindingSheet.tvSubTotal.text="₹ " + CartActivity.subValue.toString()
        if(CartActivity.deliverCharge ==20){
            bindingSheet.tvDeliveryCharge.text="₹ 20"
        }
        else if (CartActivity.deliverCharge ==10){
            bindingSheet.tvDeliveryCharge.text="₹ 10"
        }
        else
            bindingSheet.tvDeliveryCharge.text="free"


        bindingSheet.tvTotal.text= "₹ "+ amount
        bindingSheet.tvAddress.text=localCheckOut.add.toString()
        bindingSheet.tvPhone.text=localCheckOut.phone.toString()
        cartBottomSheetDialog.create()
        cartBottomSheetDialog.show()
    }





    @SuppressLint("SetTextI18n")
    override fun addObservers() {
         viewModel.checkoutOrder.observe(this){
            (binding.rvCart.adapter as CheckoutAdapter).submitList(it.list!!)
             localCheckOut=it
             localData.addAll(it.list!!)
             binding.orderText.text="Order id:- \n ${localCheckOut.orderId}"
             binding.deliveryDate.text="Delivery Date:- \n ${localCheckOut.date}"
             Log.d("checkList",""+it.list)
        }
    }

    private fun QRCode() {
        while (counter < localData.size) {
            va.add(
                "Name= ${localData[counter].products_name} ,Quantity= ${localData[counter].price}x${localData[counter].quant} ,Total=  ₹  ${localData[counter].total}")
            //Log.d("Check", "" + qqq)
            counter++
        }

        //Toast.makeText(LastPage.this,""+p,Toast.LENGTH_LONG).show();
        qrgEncoder = QRGEncoder(
            "OrderId= $id\nItem=$va\n Address=  \"\"\n Payment=",
            null,
            QRGContents.Type.TEXT,
            3000
        )
        bitmap = qrgEncoder!!.bitmap

        // Getting QR-Code as Bitmap

        // Setting Bitmap to ImageView
        binding.imgBtCart.setImageBitmap(bitmap)
    }
}