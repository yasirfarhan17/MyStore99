package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.example.networkmodule.usecase.GetAllProductsUseCase
import com.example.networkmodule.usecase.GetCartItemsCountUseCase
import com.example.networkmodule.usecase.InsertProductsUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertProductsUseCase: InsertProductsUseCase,
    private val cartItemCountUseCase: GetCartItemsCountUseCase,
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()

    private var _cartItemCount = MutableStateFlow<Int>(0)
    val cartItemCount: Flow<Int> = _cartItemCount

    init {

        getCartItemCountUseCase()
    }


    private fun getCartItemCountUseCase() {
        launch {
            cartItemCountUseCase.invoke().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        it.data?.collectLatest {
                            _cartItemCount.value = it ?: 0
                        }
                        Log.d("cartcheck", "" + it)
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                    else -> {
                        Log.d("yas", "g")
                    }
                }

            }
        }
    }

    fun insertToDB(list: ArrayList<ProductEntity>) {
        viewModelScope.launch {
            insertProductsUseCase.invoke(list)
        }
    }
}
