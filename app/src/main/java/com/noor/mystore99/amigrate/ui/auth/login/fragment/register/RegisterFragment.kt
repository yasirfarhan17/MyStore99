package com.noor.mystore99.amigrate.ui.auth.login.fragment.register

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.example.networkmodule.network.AuthResource
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.auth.AuthManager
import com.noor.mystore99.databinding.FragmentRegistetBinding

class RegisterFragment : BaseFragment<FragmentRegistetBinding, RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModels()

    private val authManager by lazy { AuthManager(requireActivity()) }

    override fun getViewModelClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayout(): Int = R.layout.fragment_registet

    override fun addObservers() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        authManager.events.observe(viewLifecycleOwner) {
            when (it) {
                is AuthResource.Error -> showToast(it.error)
                AuthResource.OtpSend -> {
                    showToast("Otp hs been sent")
                }
                AuthResource.Success -> showToast("Successfully registered")
                is AuthResource.VerificationFailed -> showToast("Verification Failed")
                else -> Log.d("SAHIL", "failed")
            }
        }
    }

    private fun initUi() {
        binding.veryfy.setOnClickListener {
            authManager.verifyOtp(authManager.mVerificationId)
        }
        binding.checkOut.setOnClickListener {
            if (checkFields()) return@setOnClickListener
            register(
                binding.etPhone.text.toString(),
                binding.etPassword.text.toString(),
            )
        }
    }

    private fun checkFields(): Boolean {
        with(binding) {
            if (etPhone.text.isNullOrEmpty()) {
                showMessage("Phone Number cannot be blank")
                return true
            }
            if (etPassword.text.isNullOrEmpty()) {
                showMessage("Password cannot be blank")
                return true
            }
            if (etCnfrmPassword.text.isNullOrEmpty()) {
                showMessage("Confirm Password cannot be blank")
                return true
            }
            if (etCnfrmPassword.text == etPassword.text) {
                showMessage("Password and Confirm password must be same")
                return true
            }
            return false
        }
    }

    fun register(phone: String, etPassword: String) {
        authManager.sendOtp(mobile = phone, false)

    }

}
