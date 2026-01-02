package com.noor.mystore99.amigrate.ui.dashboard.account.profile

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
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUser: UserDetailUseCase
) : BaseViewModel() {

    private val _userDetail = MutableLiveData<UserModel>()
    val userDetail: LiveData<UserModel> = _userDetail

    private val _updateStatus = MutableLiveData<Resource<String>>()
    val updateStatus: LiveData<Resource<String>> = _updateStatus

    fun getUserDetails(userId: String) {
        launch {
            _updateStatus.postValue(Resource.Loading()) // Re-using for loading state if needed
            getUser.invoke(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != null) {
                            _userDetail.postValue(it.data!!)
                        }
                    }
                    is Resource.Error -> {
                         _updateStatus.postValue(Resource.Error(it.message ?: "Failed to load profile"))
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateProfile(userId: String, name: String, imageUri: Uri?) {
        _updateStatus.postValue(Resource.Loading())

        if (imageUri != null) {
            // 1. Upload Image
            val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/${System.currentTimeMillis()}.jpg")
            storageRef.putFile(imageUri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // 2. Update Database with new Image URL and Name
                    updateDatabase(userId, name, uri.toString())
                }.addOnFailureListener {
                    _updateStatus.postValue(Resource.Error("Failed to get image URL: ${it.message}"))
                }
            }.addOnFailureListener {
                _updateStatus.postValue(Resource.Error("Failed to upload image: ${it.message}"))
            }
        } else {
            // Only update Name
            updateDatabase(userId, name, null)
        }
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