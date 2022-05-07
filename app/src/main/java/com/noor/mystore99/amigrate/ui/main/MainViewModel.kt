package com.noor.mystore99.amigrate.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.ProductRepository
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val productUseCase: FirebaseGetProductUseCase,
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()


    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        launch {
            productUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        _productList.postValue(it.data as ArrayList<ProductModel>)
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

    fun insertToDB(list: ArrayList<ProductEntity>) {
        viewModelScope.launch {
            productRepo.insertItems(list)
        }
    }
}
