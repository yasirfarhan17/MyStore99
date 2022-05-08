package com.noor.mystore99.amigrate.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkmodule.database.entity.ProductEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.databinding.ActivityMain3Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMain3Binding, MainViewModel>() {


    override val viewModel: MainViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_main3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavView()


    }

    private fun setNavView() {
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        binding.fabBtCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }
    }


    override fun addObservers() {
        lifecycleScope.launch {
            viewModel.cartItemCount.collect {
                Log.d("SAHIL", it.toString())
                if (it == 0) {
                    binding.clBatch.setVisible(false)
                    return@collect
                }
                binding.clBatch.setVisible(true)
                binding.tvBatch.text = it.toString()
            }

        }
    }

    fun setNavController() {

    }
}