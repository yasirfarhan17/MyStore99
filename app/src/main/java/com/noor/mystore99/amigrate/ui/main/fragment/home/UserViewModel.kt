package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.storage.PrefsUtil
import com.example.networkmodule.usecase.*
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.cart.CartViewModel
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
    private val getCart: FireBaseCartUseCase,
    private val prefsUtil: PrefsUtil,
    private var combineUseCase: CombineUseCase
) : BaseViewModel() {

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>?>()
    val categoryList = _categoryList.toLiveData()

    private var _cartFromDB = MutableLiveData<ArrayList<CartEntity>>()
    val cartFromDB = _cartFromDB.toLiveData()

    private var _productList = MutableLiveData<ArrayList<ProductEntity>>()
    val productList = _productList.toLiveData()

    private var _bannerList = MutableLiveData<ArrayList<SliderModel>?>()
    val bannerList = _bannerList.toLiveData()
    private var _insertToCart = MutableLiveData<String?>()
    val insertToCart = _insertToCart.toLiveData()



    init {
        launch {
            getAllProducts()
            getCartFromDB()
            combine()
            getBanner()
            getCategory()
        }
    }

     fun getBanner() {
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
                       // _viewState.postValue(ViewState.Success())
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

    private fun getCartFromDB() {
        launch {
            _viewState.postValue(ViewState.Loading)
            getCart().collectLatest {
                when (it) {
                    is Resource.Success -> {
                        _cartFromDB.postValue(it.data as ArrayList<CartEntity>)
                        _viewState.postValue(ViewState.Success())
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

    private fun combine(){
        launch {
            combineUseCase.invoke().collectLatest{

                    when (it) {
                        is Resource.Success -> {
                            val list = it.data?.first?.map { productModel -> productModel.toProductEntity() }

                            Log.d("insidecombi","$list ${it.data?.second}")
                            _cartFromDB.postValue(it.data?.second as ArrayList<CartEntity>)
                            _productList.postValue(list as ArrayList<ProductEntity>)
                            // _viewState.postValue(ViewState.Success())
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

    fun insertToCart(item: CartEntity) {
        launch {
            val list = ArrayList<CartEntity>()
            list.add(item)
            if (prefsUtil.Name.isNullOrEmpty()) {
                _viewState.postValue(ViewState.Error("Phone no cannot be blank"))
                return@launch
            }
            addToCartUseCase.invoke(item).collectLatest {
                _insertToCart.postValue(it.data ?: it.message)
            }
        }
    }


}