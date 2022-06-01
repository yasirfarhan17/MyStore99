package com.noor.mystore99.amigrate.ui.checkout

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.CheckOutUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel  @Inject constructor(
    private val orderUseCase: CheckOutUseCase
): BaseViewModel() {

    private var _checkoutOrder = MutableLiveData<checkOutModel>()
    val checkoutOrder = _checkoutOrder.toLiveData()

    fun getOrder(key:String,id:String){
        launch {
            _viewState.postValue(ViewState.Loading)
            orderUseCase.invoke(key,id).collect{
                when(it){
                    is Resource.Success  ->{
                        if (it.data == null) {
                                _viewState.postValue(ViewState.Error("No order Found"))
                                return@collect
                            }
                            _checkoutOrder.postValue(it.data!!)
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
}