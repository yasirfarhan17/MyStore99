package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.checkout.CheckoutActivity
import com.noor.mystore99.databinding.ActivityMyOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrder : BaseActivity<ActivityMyOrderBinding, MyOrderViewModel>(), MyOrderCallBack {
    
    override fun layoutId(): Int = R.layout.activity_my_order
    override val viewModel: MyOrderViewModel by viewModels()
    
    private lateinit var userId: String
    private lateinit var orderAdapter: MyOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order)
        
        userId = prefsUtil.Name.toString()
        
        setupUI()
        viewModel.getOrders(userId)
    }

    private fun setupUI() {
        // Back button
        binding.imgBack.setOnClickListener { finish() }
        
        // RecyclerView setup
        orderAdapter = MyOrderAdapter(this)
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(this@MyOrder)
            adapter = orderAdapter
        }
    }

    override fun addObservers() {
        // Orders list
        viewModel.checkoutOrder.observe(this) { orders ->
            orders?.let {
                // Sort by order ID descending (newest first)
                it.sortByDescending { order -> order.orderId }
                orderAdapter.submitList(it)
            }
        }
        
        // Empty state
        viewModel.hasOrders.observe(this) { hasOrders ->
            if (hasOrders) {
                binding.rvOrders.visibility = View.VISIBLE
                binding.emptyStateLayout.visibility = View.GONE
            } else {
                binding.rvOrders.visibility = View.GONE
                binding.emptyStateLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClick(orderId: String) {
        val intent = Intent(this, CheckoutActivity::class.java)
        intent.putExtra("combo", orderId)
        startActivity(intent)
    }
}