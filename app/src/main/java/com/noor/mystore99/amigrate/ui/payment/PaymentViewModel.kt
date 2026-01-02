package com.noor.mystore99.amigrate.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.CartModel
import com.example.networkmodule.model.UserModel
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.UserDetailUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    val getUser: UserDetailUseCase
) : BaseViewModel() {

    private var _userDetail = MutableLiveData<UserModel>()
    var userDetail = _userDetail.toLiveData()

    private val _pincodeStatus = MutableLiveData<Resource<String>>()
    val pincodeStatus: LiveData<Resource<String>> = _pincodeStatus

    private val _orderStatus = MutableLiveData<Resource<String>>() // Returns Order ID on success
    val orderStatus: LiveData<Resource<String>> = _orderStatus

    fun getUserDet(key: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            getUser.invoke(key).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data == null) {
                            _viewState.postValue(ViewState.Error("No details found"))
                            return@collect
                        }
                        _userDetail.postValue(it.data!!)
                        _viewState.postValue(ViewState.Success())
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                }
            }
        }
    }

    fun verifyPincode(pincode: String) {
        if (pincode.isBlank()) {
            _pincodeStatus.postValue(Resource.Error("Pincode is required"))
            return
        }
        
        _pincodeStatus.postValue(Resource.Loading())
        val ref = FirebaseDatabase.getInstance().getReference("PinCode").child(pincode)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.child("status").value == "on") {
                    _pincodeStatus.postValue(Resource.Success("Available"))
                } else {
                    _pincodeStatus.postValue(Resource.Error("Service not available at this pincode"))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _pincodeStatus.postValue(Resource.Error(error.message))
            }
        })
    }

    fun placeOrder(
        cartList: List<CartModel>,
        amount: String,
        date: String,
        address: String,
        pincode: String,
        paymentMode: String, // "cod" or "upi"
        userId: String,
        userName: String
    ) {
        _orderStatus.postValue(Resource.Loading())

        val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date())
        val orderId = currentDate1 + currentTime1
        
        val fullAddress = "$address Pincode:-$pincode"

        val model = checkOutModel(
            list = ArrayList(cartList),
            orderId = orderId,
            date = date,
            time = currentTime1,
            currentDate = currentDate,
            add = fullAddress,
            amount = amount,
            paymentMode = paymentMode,
            phone = userId,
            name = userName
        )

        // 1. Save Order
        val orderRef = FirebaseDatabase.getInstance().getReference("orderNew").child(userId).child(orderId)
        orderRef.setValue(model).addOnSuccessListener {
            // 2. Clear Cart (Firebase)
             val cartRef = FirebaseDatabase.getInstance().getReference("CartNew").child(userId)
             cartRef.removeValue()
             
             // 3. Update User Pincode
             val userRef = FirebaseDatabase.getInstance().getReference("UserNew").child(userId)
             userRef.child("pincode").setValue(pincode)

            _orderStatus.postValue(Resource.Success(orderId))
        }.addOnFailureListener {
            _orderStatus.postValue(Resource.Error(it.message ?: "Failed to place order"))
        }
    }
}