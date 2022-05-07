package com.noor.mystore99.amigrate.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.database.cart.CartEntity
import com.example.networkmodule.database.product.ProductEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.CartUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class CartViewModel  @Inject constructor(
    private val cartUseCase: CartUseCase
):BaseViewModel() {

    private var _cartFromDB = MutableLiveData<ArrayList<CartEntity>>()
    val cartFromDB = _cartFromDB.toLiveData()

    init {
        getCartFromDB()
    }


    @OptIn(InternalCoroutinesApi::class)
    fun getCartFromDB(){
        launch {
            cartUseCase().collectLatest {it->
                when(it){
                     is Resource.Success ->{
                         _cartFromDB.postValue(it.data as ArrayList<CartEntity>)
                         Log.d("cartcheck",""+it)
                     }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                    else -> {
                        Log.d("yas","g")
                    }
                }
            }
        }
    }


}