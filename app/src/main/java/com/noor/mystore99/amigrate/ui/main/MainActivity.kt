package com.noor.mystore99.amigrate.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.networkmodule.database.ProductEntity
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
        addListener()
    }

    private fun addListener() {
        with(binding) {
            fabBtCart.setOnClickListener {
                val intent = Intent(this@MainActivity, CartActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setNavView() {
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
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
}
/*
    fun showAlert() {
        alertShowing=true
        AlertDialog.Builder(this)
            .setTitle("Spinner check")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                binding.spinner.setSelection(currentSelection)
                prevSelection=currentSelection
                dialog.dismiss()
                Handler().postDelayed({
                    alertShowing=false
                },1000)

            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                binding.spinner.setSelection(prevSelection)
                dialog.dismiss()
                Handler().postDelayed({
                    alertShowing=false
                },1000)
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }
                spinner.onItemSelectedListener = object : SpinnerInteractionListener() {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    if (alertShowing) return
                    super.onItemSelected(parent, view, pos, id)
                    currentSelection = pos
                    Log.d("SAHIL", spinner.selectedItem.toString()+"--"+ prevSelection +"---"+currentSelection)
                    showAlert()
                    spinner.setSelection(prevSelection)
                }
            }


* */