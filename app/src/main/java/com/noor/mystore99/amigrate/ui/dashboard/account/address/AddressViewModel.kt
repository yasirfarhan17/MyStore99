package com.noor.mystore99.amigrate.ui.dashboard.account.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.Resource
import com.google.firebase.database.FirebaseDatabase
import com.noor.mystore99.amigrate.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor() : BaseViewModel() {

    private val _addressStatus = MutableLiveData<Resource<String>>()
    val addressStatus: LiveData<Resource<String>> = _addressStatus

    fun updateAddress(
        userId: String,
        houseNo: String,
        city: String,
        landmark: String,
        pincode: String
    ) {
        if (houseNo.isBlank() || city.isBlank() || pincode.isBlank()) {
            _addressStatus.postValue(Resource.Error("Please fill all required fields"))
            return
        }
        
        if (pincode.length < 6) {
             _addressStatus.postValue(Resource.Error("Invalid Pincode"))
             return
        }

        _addressStatus.postValue(Resource.Loading())

        // Construct the full address string to maintain compatibility
        // Format: "House No, City land Marks:- Landmark" (Matching original concatenation roughly but cleaner)
        // Original: txtInputEtAdress.text + ",  " + txtInputEtCity.text + " land Marks:- " + txtInputEtLandmark.text
        
        val fullAddress = buildString {
            append(houseNo)
            append(", ")
            append(city)
            if (landmark.isNotBlank()) {
                append(", Landmark: ")
                append(landmark)
            }
        }

        val userRef = FirebaseDatabase.getInstance().getReference("UserNew").child(userId)
        
        val updates = mapOf(
            "address" to fullAddress,
            "pincode" to pincode
        )

        userRef.updateChildren(updates).addOnSuccessListener {
            _addressStatus.postValue(Resource.Success("Address Updated Successfully"))
        }.addOnFailureListener {
            _addressStatus.postValue(Resource.Error(it.message ?: "Failed to update address"))
        }
    }
}