package com.noor.mystore99.amigrate.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.lastPage.CheckoutActivity
import com.noor.mystore99.databinding.ActivityPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        initUi()
    }


    private fun initUi() {
        with(binding) {
            checkOut.setOnClickListener {
                startActivity(Intent(this@PaymentActivity, CheckoutActivity::class.java))
            }
        }
    }

    override fun layoutId(): Int = R.layout.activity_payment

    override fun addObservers() {

    }
}