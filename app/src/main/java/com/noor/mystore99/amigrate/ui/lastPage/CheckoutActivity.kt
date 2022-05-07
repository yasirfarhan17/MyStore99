package com.noor.mystore99.amigrate.ui.lastPage

import android.os.Bundle
import androidx.activity.viewModels
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityCheckOutBinding

class CheckoutActivity : BaseActivity<ActivityCheckOutBinding, CheckoutViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val viewModel: CheckoutViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_check_out

    override fun addObservers() {
    }
}