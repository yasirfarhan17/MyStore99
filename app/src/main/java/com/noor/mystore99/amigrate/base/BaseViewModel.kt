package com.noor.mystore99.amigrate.base


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noor.mystore99.amigrate.util.toLiveData
import kotlinx.coroutines.*


abstract class BaseViewModel() : ViewModel() {


    protected val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()


    fun launch(
        code: suspend CoroutineScope.() -> Unit
    ) {
        (viewModelScope + exceptionHandler).launch (Dispatchers.IO){
            code.invoke(this)
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleFailure(throwable = exception.localizedMessage)
    }

    private fun handleFailure(throwable: String?) {
        _viewState.postValue(ViewState.Error(throwable))
    }
}