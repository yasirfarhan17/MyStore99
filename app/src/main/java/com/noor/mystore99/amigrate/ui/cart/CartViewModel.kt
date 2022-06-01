package com.noor.mystore99.amigrate.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.ClearCartItemsUseCase
import com.example.networkmodule.usecase.GetCartItemsUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val clearCartItemsUseCase: ClearCartItemsUseCase,
    private val dao: CartDao
) : BaseViewModel() {

    private var _cartFromDB = MutableLiveData<ArrayList<CartEntity>>()
    val cartFromDB = _cartFromDB.toLiveData()

    init {
        viewModelScope.launch (Dispatchers.IO){
            getCartFromDB()
        }
    }


     suspend fun getCartFromDB() {
        launch {
            _viewState.postValue(ViewState.Loading)
            getCartItemsUseCase().collectLatest {
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

    fun updateQuant(price:String,id:String,quant:String){
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(price,id,quant)
        }
    }

    fun clearCart() {
        launch {
            _viewState.postValue(ViewState.Loading)
            clearCartItemsUseCase.invoke()
            delay(300)
            _viewState.postValue(ViewState.Success())

        }
    }
    fun clear(id:String){
        launch {
            //_viewState.postValue(ViewState.Loading)
            dao.clearIndi(id)

        }
    }


}