package com.noor.mystore99.amigrate.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.networkmodule.network.AuthResource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.auth.AuthManager
import com.noor.mystore99.amigrate.ui.main.MainActivity
import com.noor.mystore99.amigrate.util.Util.flipCard
import com.noor.mystore99.amigrate.util.extension.StringExtension.isValidPhoneNumber
import com.noor.mystore99.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, MainLoginViewModel>() {

    override val viewModel: MainLoginViewModel by viewModels()

    private val authManager by lazy { AuthManager(this) }


    override fun layoutId(): Int = R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        addListener()
    }


    private fun initUi() {
        with(binding) {
            if (prefsUtil.isLoggedIn) {
                txtInputEtPhone.setText(prefsUtil.Name.toString())
                txtInputEtPassword.setText(prefsUtil.password.toString())
            }
        }
    }

    private fun addListener() {
        with(binding) {
            tvSignUp.setOnClickListener {
                flipCard(binding.cvRegister, binding.cvLogin) { showMessage(it) }
            }
            tvSignIn.setOnClickListener {
                flipCard(binding.cvLogin, binding.cvRegister) { showMessage(it) }
            }
            matBtLogin.setOnClickListener {
                doLogin()

            }
            matBtRegister.setOnClickListener {
                doRegister()
            }
        }
    }

    private fun doRegister() {
        if (binding.txtInputEtPhoneSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPhoneSignUp.error = "Phone no cannot be blank."
            return
        }
        if (binding.txtInputEtPhoneSignUp.text.toString().isValidPhoneNumber().not()) {
            binding.txtInputEtPhoneSignUp.error = "Enter correct phone number."
            return
        }
        if (binding.txtInputEtPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Password cannot be black."
            return
        }
        if (binding.txtInputEtCnfrmPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Confirm Password cannot be black."
            return
        }
        if (binding.txtInputEtCnfrmPasswordSignUp.text == binding.txtInputEtPasswordSignUp.text) {
            showToast("Password and Confirm password must be same")
            return
        }
        getOtp("+91"+binding.txtInputEtPhoneSignUp.text.toString())
    }

    private fun getOtp(phoneNumber: String) {
        authManager.sendOtp(phoneNumber, false)
    }


    private fun doLogin() {
        if (binding.txtInputEtPhone.text.isNullOrEmpty()) {
            binding.txtInputEtPhone.error = "Phone no cannot be blank."
            return
        }
        if (binding.txtInputEtPhone.text.toString().isValidPhoneNumber().not()) {
            binding.txtInputEtPhone.error = "Enter correct phone number."
            return
        }
        if (binding.txtInputEtPassword.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Password cannot be black."
            return
        }
        viewModel.doLogin(
            binding.txtInputEtPhone.text.toString(),
            binding.txtInputEtPassword.text.toString()
        )
    }


    override fun addObservers() {
        viewModel.event.observe(this) {
            when (it) {
                is AuthResource.Success -> {
                    if (binding.matCbRememberMe.isChecked) {
                        prefsUtil.isLoggedIn = true
                        prefsUtil.Name = binding.txtInputEtPhone.text.toString()
                        prefsUtil.password = binding.txtInputEtPassword.text.toString()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> {}
            }
        }
        authManager.events.observe(this) {
            when (it) {
                is AuthResource.Error -> {
                    Log.d("SAHIL",it.error)
                    showToast(it.error)
                }
                AuthResource.OtpRequired -> showMessage("Otp is required")
                AuthResource.OtpSend -> {
                    showToast("Otp Sent Successfully")
                    flipCard(binding.cvRegister, binding.cvOtp) { showToast(it) }
                }
                AuthResource.Success -> {
                    if (binding.matCbRememberMe.isChecked) {
                        prefsUtil.isLoggedIn = true
                        prefsUtil.Name = binding.txtInputEtPhoneSignUp.text.toString()
                        prefsUtil.password = binding.txtInputEtCnfrmPasswordSignUp.text.toString()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                }
                is AuthResource.VerificationFailed -> {
                    Log.d("SAHIL",it.error)
                    showToast(it.error)
                }
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authManager.clear()
    }
}

private fun AuthManager.clear() {
    if (this.activity.isDestroyed){
        //this.activity=this.activity.applicationContext
        this.activity.finish()
    }
}
