package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.checkOutModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOrderViewModel @Inject constructor() : BaseViewModel() {

    private val _checkoutOrder = MutableLiveData<ArrayList<checkOutModel>>()
    val checkoutOrder: LiveData<ArrayList<checkOutModel>> = _checkoutOrder
    
    private val _hasOrders = MutableLiveData<Boolean>()
    val hasOrders: LiveData<Boolean> = _hasOrders
    
    private val orderRef = FirebaseDatabase.getInstance().getReference("orderNew")

    fun getOrders(userId: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            
            orderRef.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val orderList = ArrayList<checkOutModel>()
                        snapshot.children.forEach { dataSnapshot ->
                            try {
                                val order = dataSnapshot.getValue(checkOutModel::class.java)
                                order?.let { orderList.add(it) }
                            } catch (e: Exception) {
                                Log.e("MyOrderViewModel", "Error parsing order: ${e.message}")
                            }
                        }
                        
                        _checkoutOrder.postValue(orderList)
                        _hasOrders.postValue(orderList.isNotEmpty())
                        _viewState.postValue(ViewState.Success())
                    } else {
                        _checkoutOrder.postValue(ArrayList())
                        _hasOrders.postValue(false)
                        _viewState.postValue(ViewState.Success())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("MyOrderViewModel", "Database error: ${error.message}")
                    _checkoutOrder.postValue(ArrayList())
                    _hasOrders.postValue(false)
                    _viewState.postValue(ViewState.Error(error.message))
                }
            })
        }
    }
}