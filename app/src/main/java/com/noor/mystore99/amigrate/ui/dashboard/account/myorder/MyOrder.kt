package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.checkout.CheckoutAdapter
import com.noor.mystore99.amigrate.ui.checkout.CheckoutViewModel
import com.noor.mystore99.amigrate.ui.payment.PaymentViewModel
import com.noor.mystore99.databinding.ActivityMyOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrder : BaseActivity<ActivityMyOrderBinding,MyOrderViewModel>() {
    override fun layoutId(): Int =R.layout.activity_my_order
    override val viewModel: MyOrderViewModel by viewModels()
    lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key=prefsUtil.Name.toString()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_my_order)
        viewModel.getOrder(key)
        getInit()
    }


    fun getInit(){

        with(binding){

            rvCart.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
            rvCart.adapter = MyOrderAdapter()

        }
    }





    override fun addObservers() {
        viewModel.checkoutOrder.observe(this){
            (binding.rvCart.adapter as MyOrderAdapter).submitList(it)
        }

    }
}