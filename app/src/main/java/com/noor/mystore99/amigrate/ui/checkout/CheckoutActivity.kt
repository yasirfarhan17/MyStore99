package com.noor.mystore99.amigrate.ui.checkout

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.checkOutModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.ActivityCheckOutBinding
import com.noor.mystore99.databinding.BottomCheckoutDialogeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CheckoutActivity : BaseActivity<ActivityCheckOutBinding, CheckoutViewModel>() {

    override val viewModel: CheckoutViewModel by viewModels()
    private val viewModelPayment: PaymentViewModel by viewModels()
    lateinit var bindingSheet: BottomCheckoutDialogeBinding
    lateinit var localCheckOut: checkOutModel

    override fun layoutId(): Int = R.layout.activity_check_out

    lateinit var key: String
    lateinit var id: String
    var localData = ArrayList<CartModel>()
    var qrgEncoder: QRGEncoder? = null
    var bitmap: Bitmap? = null
    lateinit var cartBottomSheetDialog: BottomSheetDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out)
        cartBottomSheetDialog = BottomSheetDialog(this)
        bindingSheet = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottom_checkout_dialoge,
            null,
            false
        )
        key = prefsUtil.Name.toString()
        id = intent.getStringExtra("combo").toString()
        viewModel.getOrder(key, id)
        addObservers()
        getInit()

    }


    fun getInit() {

        with(binding) {

            rvCart.layoutManager = LinearLayoutManager(this@CheckoutActivity)
            rvCart.adapter = CheckoutAdapter()
            tvViewDetails.setOnClickListener {
                showCartDetailsBottomDialog()
            }


        }
    }

    private fun showCartDetailsBottomDialog() {

        cartBottomSheetDialog.setContentView(bindingSheet.root)
        bindingSheet.tvSubTotal.text = "₹ " + CartActivity.subValue.toString()
        if (CartActivity.deliverCharge == 20) {
            bindingSheet.tvDeliveryCharge.text = "₹ 20"
        } else if (CartActivity.deliverCharge == 10) {
            bindingSheet.tvDeliveryCharge.text = "₹ 10"
        } else
            bindingSheet.tvDeliveryCharge.text = "free"


//        bindingSheet.tvTotal.text= "₹ "+ amount
        bindingSheet.tvAddress.text = localCheckOut.add.toString()
        bindingSheet.tvPhone.text = localCheckOut.phone.toString()
        cartBottomSheetDialog.create()
        cartBottomSheetDialog.show()
    }




    @SuppressLint("SetTextI18n")
    override fun addObservers() {
        viewModel.checkoutOrder.observe(this) {
            (binding.rvCart.adapter as CheckoutAdapter).submitList(it.list!!)
            localCheckOut = it
            localData.addAll(it.list!!)
            binding.orderText.text = "Order id:- \n ${localCheckOut.orderId}"
            binding.deliveryDate.text = "Delivery Date:- \n ${localCheckOut.date}"
            Log.d("checkList", "" + it.list)
            binding.tvTotalPrice.text = "₹ " + it.amount.toString()
            bindingSheet.tvPayment.text = it.paymentMode
            bindingSheet.tvTotal.text = "₹ " + it.amount.toString()
            QRCode(it.amount.toString())
        }
        viewModel.disableCancelButton.observe(this){
            if(it && binding.btCheckOut.isVisible.not()){
                binding.btCheckOut.setVisible(true)
            }else{
                binding.btCheckOut.setVisible(false)
            }
        }
        viewModelPayment.userDetail.observe(this) {
            bindingSheet.tvAddress.text = it.address
            bindingSheet.tvPhone.text = key
        }
    }

    private fun QRCode(amount: String) {

        val url = "upi://pay?pa=" +   // payment method.
                "9117151927@okbizaxis" +         // VPA number.
                "&am=" + amount +       // this param is for fixed amount (non editable).
                "&pn=Sabzi%20Taza" +      // to showing your name in app.
                "&cu=INR" +                  // Currency code.
                "&mode=02" +                 // mode O2 for Secure QR Code.
                "&orgid=189999" +            //If the transaction is initiated by any PSP app then the respective orgID needs to be passed.
                "&sign=MEYCIQC8bLDdRbDhpsPAt9wR1a0pcEssDaV" +   // Base 64 encoded Digital signature needs to be passed in this tag
                "Q7lugo8mfJhDk6wIhANZkbXOWWR2lhJOH2Qs/OQRaRFD2oBuPCGtrMaVFR23t"

        //Toast.makeText(LastPage.this,""+p,Toast.LENGTH_LONG).show();
        qrgEncoder = QRGEncoder(
            url,
            null,
            QRGContents.Type.TEXT,
            4000
        )
        bitmap = qrgEncoder!!.bitmap

        // Getting QR-Code as Bitmap

        // Setting Bitmap to ImageView
        binding.imgBtCart.setImageBitmap(bitmap)
    }
}