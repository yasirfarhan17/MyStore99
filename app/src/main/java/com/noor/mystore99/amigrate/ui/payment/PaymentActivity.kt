package com.noor.mystore99.amigrate.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.noor.mystore99.LastPage
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.lastPage.NewLastPage
import com.noor.mystore99.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding,PaymentViewModel>() {

    override val viewModel:PaymentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_payment)
        initUi()
    }


    fun initUi(){
        with(binding){
             checkOut.setOnClickListener {
                 startActivity(Intent(this@PaymentActivity,NewLastPage::class.java))
             }
        }
    }

    override fun layoutId(): Int =R.layout.activity_payment

    override fun addObservers() {

    }
}