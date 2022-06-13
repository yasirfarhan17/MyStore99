package com.noor.mystore99.amigrate.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.storage.PrefsUtil
import com.example.networkmodule.usecase.FireBaseAddToCartUseCase
import com.example.networkmodule.usecase.FirebaseGetBannerUseCase
import com.example.networkmodule.usecase.FirebaseGetCategoryUseCase
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val productUseCase: FirebaseGetProductUseCase,
    private val bannerUseCase: FirebaseGetBannerUseCase,
    private val addToCartUseCase: FireBaseAddToCartUseCase,
    private val prefsUtil: PrefsUtil
) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>?>()
    val categoryList = _categoryList.toLiveData()

    private var _productList = MutableLiveData<ArrayList<ProductEntity>>()
    val productList = _productList.toLiveData()

    private var _bannerList = MutableLiveData<ArrayList<SliderModel>?>()
    val bannerList = _bannerList.toLiveData()
    private var _insertToCart = MutableLiveData<String?>()
    val insertToCart = _insertToCart.toLiveData()


    init {
        launch {
            getAllProducts()
            getBanner()
            getCategory()
        }
    }

    private fun getBanner() {
        launch {
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
            _viewState.postValue(ViewState.Loading)
            productUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        val list = it.data?.map { productModel -> productModel.toProductEntity() }
                        _productList.postValue(list as ArrayList<ProductEntity>)
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
    }

    fun insertToCart(item: CartEntity) {
        launch {
            val list = ArrayList<CartEntity>()
            list.add(item)
            if (prefsUtil.phoneNo.isNullOrEmpty()) {
                _viewState.postValue(ViewState.Error("Phone no cannot be blank"))
                return@launch
            }
            addToCartUseCase.invoke(list).collectLatest {
                _insertToCart.postValue(it.data ?: it.message)
            }
        }
    }


}