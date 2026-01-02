package com.noor.mystore99.amigrate.ui.dashboard.account.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import coil.load
import coil.transform.CircleCropTransformation
import com.example.networkmodule.network.Resource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.dashboard.account.address.AddressActivity
import com.noor.mystore99.databinding.ActivityProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()
    
    private var userId: String = ""
    private var selectedImageUri: Uri? = null

    // Modern Image Picker
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.ivProfile.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        
        userId = prefsUtil.Name.toString()
        
        initUi()
        setupObservers()
        
        // Load initial data
        viewModel.getUserDetails(userId)
    }

    override fun layoutId(): Int = R.layout.activity_profile

    private fun initUi() {
        binding.imgBack.setOnClickListener { onBackPressed() }

        // Edit Image
        binding.ivCamera.setOnClickListener { openImagePicker() }
        binding.cvImage.setOnClickListener { openImagePicker() }

        // Manage Address
        binding.cvAddressLink.setOnClickListener {
            startActivity(Intent(this, AddressActivity::class.java))
        }

        // Save
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isBlank()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.updateProfile(userId, name, selectedImageUri)
        }
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    private fun setupObservers() {
        // User Details
        viewModel.userDetail.observe(this) { user ->
            if (user != null) {
                binding.etName.setText(user.name)
                binding.etPhone.setText(userId) // Using userId as phone per previous logic
                
                user.photo?.let { photoData ->
                    if (photoData.isNotBlank()) {
                        // Handle both URL (new) and Base64 (legacy) formats
                        if (photoData.startsWith("http")) {
                            // New format: Firebase Storage URL
                            binding.ivProfile.load(photoData) {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                                placeholder(R.drawable.usericon)
                                error(R.drawable.usericon)
                            }
                        } else {
                            // Legacy format: Base64 encoded image
                            try {
                                val decodedString = Base64.decode(photoData, Base64.DEFAULT)
                                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                binding.ivProfile.load(decodedByte) {
                                    transformations(CircleCropTransformation())
                                }
                            } catch (e: Exception) {
                                binding.ivProfile.setImageResource(R.drawable.usericon)
                            }
                        }
                    }
                }
            }
        }

        // Update Status
        viewModel.updateStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.btnSave.text = "Updating..."
                    binding.btnSave.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnSave.text = "Update Profile"
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    binding.btnSave.text = "Update Profile"
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun addObservers() {}
}