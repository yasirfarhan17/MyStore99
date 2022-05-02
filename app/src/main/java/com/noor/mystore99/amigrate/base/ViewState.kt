package com.noor.mystore99.amigrate.base


sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    data class Success(val message: String? = null) : ViewState()
    data class Error(val throwable: Throwable? = null) : ViewState()
}