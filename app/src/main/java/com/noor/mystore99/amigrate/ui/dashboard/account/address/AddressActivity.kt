package com.noor.mystore99.amigrate.ui.dashboard.account.address

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.networkmodule.network.Resource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : BaseActivity<ActivityAddressBinding, AddressViewModel>() {

    override val viewModel: AddressViewModel by viewModels()
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)
        
        userId = prefsUtil.Name.toString()

        setupListeners()
        setupObservers()
    }

    override fun layoutId(): Int = R.layout.activity_address

    private fun setupListeners() {
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSaveAddress.setOnClickListener {
            val houseNo = binding.etAddress.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val landmark = binding.etLandmark.text.toString().trim()
            val pincode = binding.etPincode.text.toString().trim()

            viewModel.updateAddress(userId, houseNo, city, landmark, pincode)
        }
    }

    private fun setupObservers() {
        viewModel.addressStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                     binding.btnSaveAddress.text = "Saving..."
                     binding.btnSaveAddress.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnSaveAddress.text = "Save Address"
                    binding.btnSaveAddress.isEnabled = true
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                is Resource.Error -> {
                    binding.btnSaveAddress.text = "Save Address"
                    binding.btnSaveAddress.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun addObservers() {}
}