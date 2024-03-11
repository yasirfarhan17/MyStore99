package com.noor.mystore99.amigrate.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.database.FirebaseDatabase
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.checkout.CheckoutActivity

class confirmOrder : AppCompatActivity() {

    lateinit var id:String
    lateinit var pincode:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        id = intent.getStringExtra("combo").toString()
        pincode = intent.getStringExtra("pincode").toString()

        Handler().postDelayed({
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("pay", "Cash on delivery")
            intent.putExtra("combo", id)
            intent.putExtra("pincode", pincode)
            intent.putExtra("flag", true)
            startActivity(intent)

        },4000)
    }
}