package com.noor.mystore99.amigrate.ui.auth.login

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
class MainLoginViewModel @Inject constructor(
    private var loginUseCase: GetLoginUseCase
) : BaseViewModel() {
    private val _event = MutableLiveData<AuthResource>()
    val event = _event.toLiveData()
    fun doLogin(phoneNumber: String, password: String) {
        launch {
            _viewState.postValue(ViewState.Loading)
            loginUseCase.invoke(phoneNumber, password).collectLatest {
                when (it) {
                    is AuthResource.Error -> _viewState.postValue(ViewState.Error(it.error))
                    AuthResource.InvalidPhoneNumber -> _viewState.postValue(ViewState.Error("Please Enter Correct Phone number"))
                    AuthResource.Loading -> _viewState.postValue(ViewState.Loading)
                    AuthResource.NoUserFound -> _viewState.postValue(ViewState.Error("No  User Found"))
                    AuthResource.OtpRequired -> {}
                    AuthResource.OtpSend -> {}
                    AuthResource.Success -> {
                        _event.postValue(AuthResource.Success)
                        _viewState.postValue(ViewState.Success())
                    }
                    AuthResource.VerificationFailed -> _viewState.postValue(ViewState.Error("Verification Failed"))
                    AuthResource.WrongPassword -> _viewState.postValue(ViewState.Error("Please enter correct password"))
                }
            }
        }
    }
}