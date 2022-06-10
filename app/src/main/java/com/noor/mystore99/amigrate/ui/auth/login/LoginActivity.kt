package com.noor.mystore99.amigrate.ui.auth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SharedMemory
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.edit
import com.example.networkmodule.model.UserModel
import com.example.networkmodule.network.AuthResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    val ref = FirebaseDatabase.getInstance()
    val users = ref.getReference("UserNew")

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
                //flipCard(binding.cvOtp,binding.cvRegister) { showToast(it) }

            }
            etOtp1.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(etOtp1.text?.length== 1){
                        etOtp2.requestFocus()
                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            etOtp2.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(etOtp2.text?.length== 1){
                        etOtp3.requestFocus()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            etOtp3.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(etOtp3.text?.length== 1){
                        etOtp4.requestFocus()
                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            etOtp4.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if(etOtp4.text?.length== 1){
                        etOtp5.requestFocus()
                    }

                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            etOtp5.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(etOtp5.text?.length== 1){
                        etOtp6.requestFocus()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            veryfy.setOnClickListener {
                if(etOtp1.text!=null && etOtp2.text!=null && etOtp3.text!=null &&etOtp4.text!=null &&etOtp5.text!=null &&etOtp6.text!=null) {
                    val otpValue:String = etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString() + etOtp5.text.toString() + etOtp6.text.toString()
                    authManager.verifyOtp(otpValue)
                    Log.d("verify",otpValue)
                }
            }
        }
    }

    private fun doRegister() {
        if (binding.txtInputEtPhoneSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPhoneSignUp.error = "Phone no cannot be blank."

        }
        if (binding.txtInputEtPhoneSignUp.text.toString().isValidPhoneNumber().not()) {
            binding.txtInputEtPhoneSignUp.error = "Enter correct phone number."
        }
        if (binding.txtInputEtPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Password cannot be black."

        }
        if (binding.txtInputEtCnfrmPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPassword.error = "Confirm Password cannot be black."

        }
        if (binding.txtInputEtCnfrmPasswordSignUp.text == binding.txtInputEtPasswordSignUp.text) {
            showToast("Password and Confirm password must be same")

        }

       // startActivity(Intent(this,MainActivity::class.java))

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
        prefsUtil.Name = binding.txtInputEtPhone.text.toString()
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
                    flipCard(binding.cvOtp,binding.cvRegister) { showToast(it) }
                    Log.d("checkOtp",""+it)

                }
                AuthResource.Success -> {
//                    if (binding.matCbRememberMe.isChecked) {
//                        prefsUtil.isLoggedIn = true
//                        prefsUtil.Name = binding.txtInputEtPhoneSignUp.text.toString()
//                        prefsUtil.password = binding.txtInputEtCnfrmPasswordSignUp.text.toString()
//                    }
                    with(binding){
                        val obj = UserModel(address = null,pincode = null,name = txtInputEtNameSignUp.text.toString(),password = txtInputEtPasswordSignUp.text.toString(),uid = FirebaseAuth.getInstance().uid!!,otpVerified = true)
                        obj?.name?.let { it1 -> Log.d("checkObj", it1) }
                        users.child(txtInputEtPhoneSignUp.text.toString()).setValue(obj)
                        showToast("Register Successfully")

                    }
//                            sendEmail(name.text.toString(), phone.text.toString())
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
