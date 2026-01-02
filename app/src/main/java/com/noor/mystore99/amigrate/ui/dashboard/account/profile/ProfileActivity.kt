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
                // Check file size (max 10MB)
                val fileSize = contentResolver.openInputStream(uri)?.available() ?: 0
                val maxSize = 10 * 1024 * 1024 // 10MB in bytes
                
                if (fileSize > maxSize) {
                    Toast.makeText(this, R.string.profile_image_too_large, Toast.LENGTH_LONG).show()
                    return@registerForActivityResult
                }
                
                selectedImageUri = uri
                binding.ivProfile.load(uri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                
                // Visual feedback: increase elevation to show pending upload
                binding.cvImage.cardElevation = 12f
                binding.ivCamera.setImageResource(R.drawable.ic_camera_alt_black_24dp)
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
            
            // Comprehensive validation
            when {
                name.isBlank() -> {
                    Toast.makeText(this, R.string.profile_name_empty, Toast.LENGTH_SHORT).show()
                }
                name.length < 2 -> {
                    Toast.makeText(this, R.string.profile_name_too_short, Toast.LENGTH_SHORT).show()
                }
                name.length > 50 -> {
                    Toast.makeText(this, R.string.profile_name_too_long, Toast.LENGTH_SHORT).show()
                }
                !name.matches(Regex("^[a-zA-Z\\s]+$")) -> {
                    Toast.makeText(this, R.string.profile_name_invalid_chars, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.updateProfile(userId, name, selectedImageUri)
                }
            }
        }
    }
    
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    private fun setupObservers() {
        // Loading State
        viewModel.loadingStatus.observe(this) { isLoading ->
            // Could show/hide a progress bar here if needed
            // For now, it just prevents the confusing "Updating..." text
        }
        
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
                    binding.btnSave.text = getString(R.string.profile_updating)
                    binding.btnSave.isEnabled = false
                }
                is Resource.Success -> {
                    binding.btnSave.text = getString(R.string.profile_update_button)
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, resource.data, Toast.LENGTH_SHORT).show()
                    
                    // Refresh user data to show updated info
                    viewModel.getUserDetails(userId)
                    
                    // Reset selected image since it's now uploaded
                    selectedImageUri = null
                    
                    // Reset visual indicator
                    binding.cvImage.cardElevation = 4f
                }
                is Resource.Error -> {
                    binding.btnSave.text = getString(R.string.profile_update_button)
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun addObservers() {}
}