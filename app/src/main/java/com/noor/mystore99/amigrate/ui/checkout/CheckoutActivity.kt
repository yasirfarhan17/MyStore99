package com.noor.mystore99.amigrate.ui.checkout

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.checkOutModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.myorder.MyOrder
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
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
    lateinit var id1: String
    lateinit var pincode: String
    var localData = ArrayList<CartModel>()
    var bitmap: Bitmap? = null
    lateinit var cartBottomSheetDialog: BottomSheetDialog
     var ref = FirebaseDatabase.getInstance().reference
     var flag:Boolean=false


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
        id1 = intent.getStringExtra("combo").toString()
        pincode = intent.getStringExtra("pincode").toString()
        flag = intent.getBooleanExtra("flag",false)
        viewModel.getOrder(key, id1)
        val ref=FirebaseDatabase.getInstance().getReference("orderNew").child(key).child(id1)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var item: checkOutModel = snapshot.getValue(checkOutModel::class.java)!!

                    (binding.rvCart.adapter as CheckoutAdapter).submitList(item.list!!)
                    localCheckOut = item
                    localData.addAll(item.list!!)
                    binding.orderText.text = "Order id:- \n ${localCheckOut.orderId}"
                    binding.deliveryDate.text = "Delivery Date:- \n ${localCheckOut.date}"
                    Log.d("checkList", "" + item.list)
                    binding.tvTotalPrice.text = "₹ " + item.amount.toString()
                    bindingSheet.tvPayment.text = item.paymentMode
                    bindingSheet.tvTotal.text = "₹ " + item.amount.toString()
                    QRCode(item.amount.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        addObservers()
        getInit()

    }


    override fun onBackPressed() {

        if(flag){
        startActivity(Intent(this@CheckoutActivity,com.noor.mystore99.amigrate.ui.main.MainActivity::class.java))
        flag=false
            }
        else
            super.onBackPressed()
    }

    fun getInit() {

        with(binding) {

            rvCart.layoutManager = LinearLayoutManager(this@CheckoutActivity)
            rvCart.adapter = CheckoutAdapter()
            tvViewDetails.setOnClickListener {
                showCartDetailsBottomDialog()
            }


            btCheckOut.setOnClickListener(View.OnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(
                    this@CheckoutActivity
                )

                // set title
                alertDialogBuilder.setTitle("Cancel Order")

                // set dialog message
                alertDialogBuilder
                    .setMessage("Do you really want to cancel order?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        ref = FirebaseDatabase.getInstance().getReference("orderNew").child(
                           key
                        ).child(id1)
                        ref.removeValue()
                        val intent = Intent(this@CheckoutActivity, MyOrder::class.java)
                        startActivity(intent)
                        ref = FirebaseDatabase.getInstance().getReference("User").child(key)
//                        ref.addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    name = dataSnapshot.child("name").value.toString()
//                                    phone = dataSnapshot.child("phone").value.toString()
//                                }
//
//                               // sendEmail(zz, name, phone)
//
//                            }
//
//                            override fun onCancelled(databaseError: DatabaseError) {}
//                        })
                        Toast.makeText(
                            this@CheckoutActivity,
                            "Order cancel successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("No") { dialog, id -> // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel()
                    }

                // create alert dialog
                val alertDialog = alertDialogBuilder.create()

                // show it
                alertDialog.show()
            })
            back.setOnClickListener {
                onBackPressed()
            }

        }
    }

    private fun showCartDetailsBottomDialog() {

        cartBottomSheetDialog.setContentView(bindingSheet.root)
       val Price= localCheckOut.amount
        var subPrice=0
        var deliveryCharge=0
        if(Price?.toInt() in 401..1000) {
            deliveryCharge =10
        }
        else if(Price?.toInt() in 100..400) {
            deliveryCharge =20
        }
        else {
            deliveryCharge =0
        }
    subPrice= Price?.toInt()?.minus(deliveryCharge)!!
        bindingSheet.tvDeliveryCharge.text=deliveryCharge.toString()
        bindingSheet.tvSubTotal.text=subPrice.toString()

//        bindingSheet.tvTotal.text= "₹ "+ amount
        bindingSheet.tvAddress.text  = localCheckOut.add.toString()
        bindingSheet.tvPhone.text = localCheckOut.phone.toString()
        cartBottomSheetDialog.create()
        cartBottomSheetDialog.show()
    }




    @SuppressLint("SetTextI18n")
    override fun addObservers() {
//        viewModel.checkoutOrder.observe(this) {
//            (binding.rvCart.adapter as CheckoutAdapter).submitList(it.list!!)
//            localCheckOut = it
//            localData.addAll(it.list!!)
//            binding.orderText.text = "Order id:- \n ${localCheckOut.orderId}"
//            binding.deliveryDate.text = "Delivery Date:- \n ${localCheckOut.date}"
//            Log.d("checkList", "" + it.list)
//            binding.tvTotalPrice.text = "₹ " + it.amount.toString()
//            bindingSheet.tvPayment.text = it.paymentMode
//            bindingSheet.tvTotal.text = "₹ " + it.amount.toString()
//            QRCode(it.amount.toString())
//        }
//        viewModel.disableCancelButton.observe(this){
//            if(it && binding.btCheckOut.isVisible.not()){
//                binding.btCheckOut.setVisible(true)
//            }else{
//                binding.btCheckOut.setVisible(false)
//            }
//        }
        viewModelPayment.userDetail.observe(this) {
            bindingSheet.tvAddress.text = it.address
            bindingSheet.tvPhone.text = key
        }
    }

    override fun onResume() {
        super.onResume()





        val currentTime = SimpleDateFormat("HHmmss", Locale.getDefault()).format(Date())
        val timeSub = currentTime.substring(0, 5)
        val timeReplace = timeSub.replace("[^a-zA-Z0-9]".toRegex(), "")
        val timeCon: Int = timeReplace.toInt()

        val currentDate = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(
            Date()
        )


        ref = FirebaseDatabase.getInstance().getReference("orderNew").child(
            key
        ).child(id1)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val date=snapshot.child("currentDate").value.toString()
                    Log.d("insideCheckOut",currentDate+" "+date)
                    if(currentDate.equals(date)){
                        val time1=snapshot.child("time").value.toString()
                        Log.d("insideCheckOut",currentTime+" "+time1)
                        if(currentTime.toInt()-time1.toInt()>1000){
                            binding.btCheckOut.visibility=View.INVISIBLE
                        }
                    }
                    else{
                        binding.btCheckOut.visibility=View.INVISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

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

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 4000, 4000)
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding.imgBtCart.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}