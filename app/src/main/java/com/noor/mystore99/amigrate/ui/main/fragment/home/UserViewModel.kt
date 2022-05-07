package com.noor.mystore99.amigrate.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FirebaseGetBannerUseCase
import com.example.networkmodule.usecase.FirebaseGetCategoryUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val categoryUseCase: FirebaseGetCategoryUseCase,
    private val bannerUseCase: FirebaseGetBannerUseCase,
    private val cartDao: CartDao
) : BaseViewModel() {
    private var _bannerList = MutableLiveData<ArrayList<SliderModel>>()
    val bannerList = _bannerList.toLiveData()

    private var _categoryList = MutableLiveData<ArrayList<CategoryModel>>()
    val categoryList = _categoryList.toLiveData()

    init {
        getCategory()
        getBanner()
    }

    private fun getBanner() {
        launch {
            bannerUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        _bannerList.postValue(it.data as ArrayList<SliderModel>)
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
        viewModelScope.launch {
            val arr = ArrayList<CartEntity>()
            arr.addAll(cartDao.getAllCartProduct())
            arr.add(item)
            cartDao.insertAllCartProduct(arr)
        }
    }

}