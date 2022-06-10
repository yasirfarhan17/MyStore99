package com.noor.mystore99.amigrate.ui.upi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.checkOutModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.noor.mystore99.SendMail
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
import com.noor.mystore99.amigrate.ui.checkout.CheckoutActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.cartItem
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewUPIPay : AppCompatActivity(), PaymentStatusListener {


    val viewModel: PaymentViewModel by viewModels()
    val viewModelCart: CartViewModel by viewModels()

    var upiFlag: Boolean = false
    lateinit var combo: String
    lateinit var key: String
    lateinit var amount: String
    lateinit var address: String
    lateinit var date: String
    var list = ArrayList<CartModel>()
    lateinit var ref1: DatabaseReference
    val UPI_PAYMENT = 0


    val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("key").toString()
        val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(
            Date()
        )
        combo = currentDate1 + currentTime1
        //amount=getIntent().getStringExtra("amount");
        amount = "10.00"
        address = intent.getStringExtra("address").toString()
        date = intent.getStringExtra("date").toString()

        ref1 = FirebaseDatabase.getInstance().getReference("orderNew").child(key)
        viewModel.getUserDet(key)


        //Toast.makeText(upiPay.this,val,Toast.LENGTH_SHORT)
        //
        // 9117151927@okbizaxis;
        payUsingUpi(
            "SabziTaza", "9117151927@okbizaxis",
            "Sabzi Taza Payment", "1"
        )
        //("1.00","9117151927@okbizaxis","Sabzi Taza","Sabzi Taza Payment",combo)
    }

    private fun payUsingUpi(name: String, upiId: String, note: String, amount: String) {
        Log.e("main ", "name $name--up--$upiId--$note--$amount")

        val uri=Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("mc", "BCR2DN6T6W7PZD7")
                .appendQueryParameter("mode", "04")
                .appendQueryParameter("orgid", "189999")
                .appendQueryParameter("sign", "MEYCIQC8bLDdRbDhpsPAt9wR1a0pcEssDaV")
                .appendQueryParameter("tr", combo)
                .appendQueryParameter("tn", note)
                 //.appendQueryParameter("refUrl", "blueapp")
                .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                this,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("main ", "response $resultCode")
        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.e("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    if (trxt != null) {
                        dataList.add(trxt)
                    }
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null")
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    open fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this)) {
            var str = data[0]
            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&").toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=").toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT).show()
                setValueToFirebase()
                startActivity(Intent(this@NewUPIPay, CheckoutActivity::class.java))
                viewModelCart.clearCart()

                finish()
            } else {
                Toast.makeText(
                    this,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
                finish()
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                this,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun setValueToFirebase() {
        val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(
            Date()
        )
        combo = currentDate1 + currentTime1

        val model = checkOutModel(list, combo, date, currentTime1, address, amount, "upi", key)

        ref1.child(combo).setValue(model)

    }

    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }

    open fun sendEmail(
        order: String,
        list: ArrayList<cartItem>,
        date: String,
        add: String,
        name: String,
        phone: String,
        payToatl: String,
        op: String
    ) {
        //Getting content for email
        val `val` = ArrayList<String>()
        for (i in list.indices) {
            if (list[i].weight == "Per Kg") {
                `val`.add(
                    """Item: ${list[i].name}	(1 Kg)	 Quantity :${list[i].quant}	 price: ${list[i].total}
"""
                )
            }
        }
        val str = """Order$order			Order Date:${date}Name $name			Phone: $phone
$`val`
Address :$add
Total: $payToatl
Payment: $op"""
        //Creating SendMail object
        val sm = SendMail(this, "sabzitaza90@gmail.com", "Order", str)

        //Executing sendmail to send email
        sm.execute()
    }


    private fun makePayment(amount:String,  upi:String,  name:String, desc:String, transactionId :String) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
         val easyUpiPayment = EasyUpiPayment.Builder()
            .with(this)
            // on below line we are adding upi id.
            .setPayeeVpa(upi)
            // on below line we are setting name to which we are making oayment.
            .setPayeeName(name)
            // on below line we are passing transaction id.
            .setTransactionId(transactionId)
            // on below line we are passing transaction ref id.
            .setTransactionRefId(transactionId)
            // on below line we are adding description to payment.
            .setDescription(desc)
            // on below line we are passing amount which is being paid.
            .setAmount(amount)
            // on below line we are calling a build method to build this ui.
            .build()
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment()
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this)
    }


     override fun onTransactionCompleted(transactionDetails:TransactionDetails) {
        // on below line we are getting details about transaction when completed.
        var transcDetails :String = transactionDetails.status.toString() + "\n" + "Transaction ID : " + transactionDetails.transactionId
         //transactionDetailsTV.setVisibility(View.VISIBLE);
        // on below line we are setting details to our text view.
        //transactionDetailsTV.setText(transcDetails);
    }


     override fun onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show()
         setValueToFirebase()
         startActivity(Intent(this@NewUPIPay, CheckoutActivity::class.java))
         viewModelCart.clearCart()
    }


     override fun onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("TAG", "TRANSACTION SUBMIT")
     }


    override fun onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show()
    }


    override fun onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show()
    }


    override fun onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show()
    }
}
