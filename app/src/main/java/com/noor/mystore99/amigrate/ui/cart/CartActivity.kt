package com.noor.mystore99.amigrate.ui.cart

import android.os.Bundle
import androidx.activity.viewModels
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>() {


    override val viewModel: CartViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun addObservers() {
    }


}