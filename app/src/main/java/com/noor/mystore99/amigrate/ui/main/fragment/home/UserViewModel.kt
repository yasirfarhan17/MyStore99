package com.noor.mystore99.amigrate.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import com.example.networkmodule.usecase.FirebaseGetBannerUseCase
import com.example.networkmodule.usecase.FirebaseGetCategoryUseCase
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.example.networkmodule.usecase.InsertCartItemUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val insertToCartUseCase: InsertCartItemUseCase,
    private val cartRepo: CartRepository,
    private val productUseCase: FirebaseGetProductUseCase,
    private val bannerUseCase: FirebaseGetBannerUseCase

) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>?>()
    val categoryList = _categoryList.toLiveData()


    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()

    private var _bannerList = MutableLiveData<ArrayList<SliderModel>?>()
    val bannerList = _bannerList.toLiveData()


    init {
        launch {
            _viewState.postValue(ViewState.Loading)
            getAllProducts()
            getBanner()
            getCategory()
        }
    }

    private fun getBanner() {
        launch {
            _viewState.postValue(ViewState.Loading)
            bannerUseCase.invoke().collectLatest {
                if (it.data.isNullOrEmpty()) {
                    _bannerList.postValue(null)
                    return@collectLatest
                }
                _bannerList.postValue(it.data as ArrayList<SliderModel>)

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
                if (it.data.isNullOrEmpty()) {
                    _categoryList.postValue(null)
                    return@collect
                }
                _categoryList.postValue(it.data as ArrayList<CategoryModel>)
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