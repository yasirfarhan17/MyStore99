package com.noor.mystore99.amigrate.ui.main

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noor.mystore99.BuildConfig
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.cart.CartActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.amigrate.util.Util.setVisible
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.databinding.ActivityMain3Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMain3Binding, MainViewModel>() {


    override val viewModel: MainViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()


    companion object {
        private const val TAG = "MainActivity"
    }

    override fun layoutId(): Int = R.layout.activity_main3

    protected val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle Back Press using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })

        if (checkInternet()) {
            viewModel.checkAppVersion(BuildConfig.VERSION_CODE)
            setNavView()
            // onResume logic for cart is now handled by observers
        } else {
            Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }


    private fun showUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle("SabziTaza Update")
            .setMessage("Please update SabziTaza to the latest version.")
            .setCancelable(false)
            .setPositiveButton("Update") { _, _ ->
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.noor.mystore99")
                )
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Do you really want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity() // Closes all activities in the task
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setNavView() {
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        binding.fabBtCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }


    override fun addObservers() {
        lifecycleScope.launch {
            userViewModel.cartFromDB.observe(this@MainActivity) { cartItems ->
                if (cartItems.isNotEmpty()) {
                    val count = cartItems.size
                    binding.clBatch.setVisible(true)
                    binding.tvBatch.text = count.toString()
                } else {
                    binding.clBatch.setVisible(false)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateRequired.collect { required ->
                if (required) {
                    showUpdateDialog()
                }
            }
        }
    }

    private fun checkInternet(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}