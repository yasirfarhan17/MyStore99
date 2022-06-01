package com.noor.mystore99.amigrate.ui.payment

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.UserDetailUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.example.networkmodule.model.UserModel
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel  @Inject constructor(
    val getUser:UserDetailUseCase
):BaseViewModel() {
    private var _userDetail = MutableLiveData<UserModel>()
    var userDetail = _userDetail.toLiveData()


    fun getUserDet(key:String){
        launch {
            _viewState.postValue(ViewState.Loading)
            getUser.invoke(key).collect{
                when(it){
                    is Resource.Success ->{
                        if (it.data == null) {
                            _viewState.postValue(ViewState.Error("No Product Found"))
                            return@collect
                        }
                        _userDetail.postValue(it.data!!)
                        _viewState.postValue(ViewState.Success())
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }

                }                }
            }

    }
}