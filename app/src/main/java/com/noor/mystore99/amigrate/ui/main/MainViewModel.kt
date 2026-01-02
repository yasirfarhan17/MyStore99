package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.example.networkmodule.usecase.GetCartItemsCountUseCase
import com.example.networkmodule.usecase.GetProductcountUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartItemCountUseCase: GetCartItemsCountUseCase,
    private val productUseCase: FirebaseGetProductUseCase,
    private val productItemsCountUseCase: GetProductcountUseCase
) : BaseViewModel() {

    private var _cartItemCount = MutableStateFlow(0)
    val cartItemCount: Flow<Int> = _cartItemCount

    private var _productItemCount = MutableStateFlow(0)
    val productItemCount: Flow<Int> = _productItemCount

    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()

    private val _updateRequired = MutableStateFlow(false)
    val updateRequired: Flow<Boolean> = _updateRequired

    init {
        getCartItemCountUseCase()
    }

    fun checkAppVersion(currentVersionCode: Int) {
        val versionRef =
            com.google.firebase.database.FirebaseDatabase.getInstance().getReference("version")
                .child("version")
        versionRef.addListenerForSingleValueEvent(object :
            com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                if (snapshot.exists()) {
                    val serverVersionCode = snapshot.value.toString().toIntOrNull() ?: 0
                    if (currentVersionCode < serverVersionCode) {
                        _updateRequired.value = true
                    }
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("MainViewModel", "Version check error: ${error.message}")
            }
        })
    }


    private fun getCartItemCountUseCase() {
        launch {
            cartItemCountUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.collectLatest { count ->
                            _cartItemCount.value = count ?: 0
                        }
                        Log.d("CartCheck", "" + it)
                    }

                    else -> {}
                }
            }
        }
    }
}
