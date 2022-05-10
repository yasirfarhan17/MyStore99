package com.example.networkmodule.network

sealed class AuthResource(val message: String? = null) {
    object WrongPassword : AuthResource()
    object InvalidPhoneNumber : AuthResource()
    object Loading : AuthResource()
    object NoUserFound : AuthResource()
    object Success : AuthResource()
    object OtpSend : AuthResource()
    object OtpRequired : AuthResource()
    object VerificationFailed : AuthResource()
    data class Error(var error: String) : AuthResource(error)
}