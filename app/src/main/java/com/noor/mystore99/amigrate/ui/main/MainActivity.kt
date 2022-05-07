package com.noor.mystore99.amigrate.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkmodule.database.entity.ProductEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.databinding.ActivityMain3Binding
import dagger.hilt.android.AndroidEntryPoint

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
       /* val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_home, R.id.navigation_user))
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
        
        binding.fabBtCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }
    }


    override fun addObservers() {
        viewModel.productList.observe(this) {
            val arr = ArrayList<ProductEntity>()
            it.onEach { item ->

                val productEntity = ProductEntity(
                    item.products_name!!,
                    item.price,
                    item.img,
                    item.quant,
                    item.hindiName,
                    item.stock
                )
                Log.d("yasir ", "" + item)
                arr.add(productEntity)


            }
            Log.d("yasir ", "" + arr)
            viewModel.insertToDB(arr)

        }
    }

    fun setNavController() {

    }
}