package com.noor.mystore99.amigrate.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.networkmodule.network.AuthResource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.ui.main.MainActivity
import com.noor.mystore99.amigrate.util.Util.flipCard
import com.noor.mystore99.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, MainLoginViewModel>() {

    override val viewModel: MainLoginViewModel by viewModels()


    override fun layoutId(): Int = R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        addListener()

    }


    private fun initUi() {

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
        }
    }

    private fun doLogin() {
        if (binding.txtInputEtPhone.text.isNullOrEmpty()) {
            binding.txtInputEtPhone.error = "Phone No cannot be blank"
            return
        }
        if (binding.txtInputEtPhone.text.toString()
                .matches(Regex("^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$")).not()
        ) {
            binding.txtInputEtPhone.error = "Enter correct phone number"
            return
        }
        if (binding.txtInputEtPassword.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Password cannot be black"
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
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> {}
            }
        }
    }
}
