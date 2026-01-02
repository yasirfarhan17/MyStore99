package com.noor.mystore99.amigrate.ui.dashboard.account.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.UserModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.UserDetailUseCase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.noor.mystore99.amigrate.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getUser: UserDetailUseCase
) : BaseViewModel() {

    private val _userDetail = MutableLiveData<UserModel>()
    val userDetail: LiveData<UserModel> = _userDetail

    private val _updateStatus = MutableLiveData<Resource<String>>()
    val updateStatus: LiveData<Resource<String>> = _updateStatus
    
    private val _loadingStatus = MutableLiveData<Boolean>()
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    fun getUserDetails(userId: String) {
        launch {
            _loadingStatus.postValue(true)
            getUser.invoke(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) {
                            _userDetail.postValue(it.data!!)
                        }
                        _loadingStatus.postValue(false)
                    }
                    is Resource.Error -> {
                        _loadingStatus.postValue(false)
                        // Could show error via Snackbar or separate error LiveData
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateProfile(userId: String, name: String, imageUri: Uri?) {
        _updateStatus.postValue(Resource.Loading())

        if (imageUri != null) {
            // 1. Compress and Upload Image
            try {
                val compressedImage = compressImage(imageUri)
                val storageRef = FirebaseStorage.getInstance().reference
                    .child("profile_images/${System.currentTimeMillis()}.jpg")
                
                storageRef.putBytes(compressedImage).addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // 2. Update Database with new Image URL and Name
                        updateDatabase(userId, name, uri.toString())
                    }.addOnFailureListener {
                        _updateStatus.postValue(Resource.Error("Failed to get image URL: ${it.message}"))
                    }
                }.addOnFailureListener {
                    _updateStatus.postValue(Resource.Error("Failed to upload image: ${it.message}"))
                }
            } catch (e: Exception) {
                _updateStatus.postValue(Resource.Error("Failed to process image: ${e.message}"))
            }
        } else {
            // Only update Name
            updateDatabase(userId, name, null)
        }
    }
    
    private fun compressImage(uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        
        // Calculate scaling to max 1024x1024
        val maxSize = 1024
        val ratio = kotlin.math.min(
            maxSize.toFloat() / originalBitmap.width,
            maxSize.toFloat() / originalBitmap.height
        )
        
        val width = (ratio * originalBitmap.width).toInt()
        val height = (ratio * originalBitmap.height).toInt()
        
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
        
        // Compress to JPEG with 80% quality
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        
        // Clean up
        originalBitmap.recycle()
        scaledBitmap.recycle()
        
        return outputStream.toByteArray()
    }

    private fun updateDatabase(userId: String, name: String, photoUrl: String?) {
        val userRef = FirebaseDatabase.getInstance().getReference("UserNew").child(userId)
        val updates = mutableMapOf<String, Any>("name" to name)
        
        if (photoUrl != null) {
            updates["photo"] = photoUrl
        }

        userRef.updateChildren(updates).addOnSuccessListener {
            _updateStatus.postValue(Resource.Success("Profile Updated Successfully"))
        }.addOnFailureListener {
            _updateStatus.postValue(Resource.Error("Failed to update profile: ${it.message}"))
        }
    }
}