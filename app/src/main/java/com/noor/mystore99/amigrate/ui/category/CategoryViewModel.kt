package com.noor.mystore99.amigrate.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.usecase.FireBaseCategoryUseCase
import com.example.networkmodule.usecase.InsertCartItemUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCase: FireBaseCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val cartRepo: CartRepository
) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<ProductModel>>()
    var categoryList = _categoryList.toLiveData()


    fun getAllCategory(productName: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            categoryUseCase.invoke(productName).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()) {
                            _viewState.postValue(ViewState.Error("No Product Found"))
                            return@collect
                        }
                        _categoryList.postValue(it.data as ArrayList<ProductModel>)
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

    fun insertToCartDb(item: CartEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val arr = ArrayList<CartEntity>()
            arr.addAll(cartRepo.getCartItem())
            arr.add(item)
            insertToCartUseCase.invoke(arr)
        }
    }

}