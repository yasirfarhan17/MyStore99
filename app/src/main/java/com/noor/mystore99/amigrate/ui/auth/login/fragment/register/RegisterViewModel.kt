package com.noor.mystore99.amigrate.ui.auth.login.fragment.register

import androidx.fragment.app.FragmentActivity
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.ui.auth.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(

) : BaseViewModel() {

    private lateinit var authManager: AuthManager
    fun register(phone: String, etPassword: String, registerFragment: FragmentActivity) {
        authManager= AuthManager(registerFragment)
        authManager.sendOtp(mobile =phone,false)
    }

}