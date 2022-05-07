package com.noor.mystore99.amigrate.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.payment.PaymentActivity
import com.noor.mystore99.databinding.ActivityNewCartBinding
import com.noor.mystore99.databinding.CartViewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityNewCartBinding,CartViewModel>() {

    override val viewModel:CartViewModel by viewModels()
    override fun layoutId(): Int = R.layout.activity_new_cart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_new_cart)

        initUi()
    }

    private fun initUi() {

        with(binding){
            CartRecyclerView3.layoutManager=LinearLayoutManager(this@CartActivity)
            CartRecyclerView3.adapter=CartAdapter()

            checkOut.setOnClickListener {
                startActivity(Intent(this@CartActivity,PaymentActivity::class.java))
            }

        }



    }

    override fun addObservers() {
        viewModel.cartFromDB.observe(this){
            (binding.CartRecyclerView3.adapter as CartAdapter).submitList(it)
            Log.d("cartcheck",""+it)
        }

    }


}