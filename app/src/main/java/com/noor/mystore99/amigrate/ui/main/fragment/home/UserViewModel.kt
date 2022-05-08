package com.noor.mystore99.amigrate.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.usecase.FirebaseGetCategoryUseCase
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.example.networkmodule.usecase.InsertCartItemUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val cartRepo: CartRepository,
    private val productUseCase: FirebaseGetProductUseCase,

    ) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>>()
    val categoryList = _categoryList.toLiveData()


    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()


    init {
        launch {
            withContext(Dispatchers.Default) {
                getAllProducts()
                getCategory()
            }
        }
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

    private fun getCategory() {
        launch {
            categoryUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        _categoryList.postValue(it.data as ArrayList<CategoryModel>)
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
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