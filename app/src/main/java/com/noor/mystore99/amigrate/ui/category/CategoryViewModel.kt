package com.noor.mystore99.amigrate.ui.category

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FireBaseCategoryUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(

    private val categoryUsecase:FireBaseCategoryUseCase
):BaseViewModel(){


//    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
//    val categoryproductList = _productList.toLiveData()

    private var _productList = MutableStateFlow<ArrayList<ProductModel>>(ArrayList())
    val categoryproductList : Flow<ArrayList<ProductModel>> = _productList

    fun getAllCategory(productName:String){
        launch {
            categoryUsecase.invoke(productName).collect{
                when(it){
                    is Resource.Success ->{
                        _productList.value=(it.data as ArrayList<ProductModel>)
                    //    _productList.setValue(it.data as ArrayList<ProductModel>)
                        _viewState.postValue(ViewState.Success())
                        Log.d("yasir__",""+it.data)
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
    }