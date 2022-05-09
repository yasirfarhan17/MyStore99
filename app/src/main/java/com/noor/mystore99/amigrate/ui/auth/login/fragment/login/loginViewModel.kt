package com.noor.mystore99.amigrate.ui.auth.login.fragment.login

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.usecase.GetLoginUseCase
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@HiltViewModel
class loginViewModel @Inject constructor(
    private val loginUseCase: GetLoginUseCase
) : BaseViewModel() {

    private val _event = MutableLiveData<AuthResource>()
    val event = _event.toLiveData()
    fun doLogin(phoneNumber: String, password: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            loginUseCase.invoke(phoneNumber, password).collectLatest {
                _event.postValue(it)
            }
        }
    }

}