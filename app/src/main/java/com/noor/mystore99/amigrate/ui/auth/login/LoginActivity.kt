package com.noor.mystore99.amigrate.ui.auth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SharedMemory
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.networkmodule.model.NotificationModel
import com.example.networkmodule.model.UserModel
import com.example.networkmodule.network.AuthResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.auth.AuthManager
import com.noor.mystore99.amigrate.ui.main.MainActivity
import com.noor.mystore99.amigrate.util.Util.flipCard
import com.noor.mystore99.amigrate.util.extension.StringExtension.isValidPhoneNumber
import com.noor.mystore99.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import com.noor.mystore99.amigrate.util.PushNotification

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
            // Animation
            cvLogin.alpha = 0f
            cvLogin.translationY = 100f
            cvLogin.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(200).start()
            
            labelHeader.alpha = 0f
            labelHeader.animate().alpha(1f).setDuration(800).start()

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
//                val intent=Intent(this@LoginActivity,PushNotification::class.java)
//                intent.putExtra("title","title")
//                intent.putExtra("message","title")
//                startActivity(intent)
//                val obj=NotificationModel("title","message")
//                ref.reference.child("Notification").setValue(obj)
                doLogin()
//                val intent=Intent(this@LoginActivity,MainActivity::class.java)
//                startActivity(intent)



            }
            matBtSetlogin.setOnClickListener {
                setPassWord()
            }
            cvLoginPage.setOnClickListener {
                flipCard(cvLogin,cvForget){showMessage(it)}
            }
            tvForget.setOnClickListener {
                flipCard(binding.cvForget,cvLogin){showMessage(it)}
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

            btnBackRegister.setOnClickListener {
                flipCard(binding.cvLogin, binding.cvRegister) { showMessage(it) }
            }
            btnBackForget.setOnClickListener {
                 flipCard(binding.cvLogin, binding.cvForget) { showMessage(it) }
            }

            onBackPressedDispatcher.addCallback(this@LoginActivity, object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    with(binding) {
                        when {
                            cvRegister.visibility == android.view.View.VISIBLE -> {
                                flipCard(cvLogin, cvRegister) { showMessage(it) }
                            }
                            cvForget.visibility == android.view.View.VISIBLE -> {
                                flipCard(cvLogin, cvForget) { showMessage(it) }
                            }
                            cvOtp.visibility == android.view.View.VISIBLE -> {
                                // Logic to decide where to go back from OTP?? 
                                // For now, let's assume back to Register which seems to be the main entry per existing logic or Login
                                // Reviewing existing code: OTP can come from Register or Forget (Set Password).
                                // Current safe bet for "confusion" fix: Go back to Login or Register.
                                // Let's check: if (txtInputEtNameSignUp.text.isNullOrEmpty()) -> likely from Forget/SetPassword
                                if (txtInputEtNameSignUp.text.isNullOrEmpty()) {
                                     flipCard(cvForget, cvOtp) { showMessage(it) }
                                } else {
                                     flipCard(cvRegister, cvOtp) { showMessage(it) }
                                }
                            }
                            else -> {
                                isEnabled = false
                                onBackPressedDispatcher.onBackPressed()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun setPassWord(){
        if (binding.txtInputEtSetPhone.text.isNullOrEmpty()) {
            binding.txtInputEtSetPhone.error = "Phone no cannot be blank."

        }
        if (binding.txtInputEtSetPhone.text.toString().isValidPhoneNumber().not()) {
            binding.txtInputEtSetPhone.error = "Enter correct phone number."
        }
        if (binding.txtInputEtSetpassword.text.isNullOrEmpty()) {
            binding.txtInputEtSetpassword.error = "Password cannot be black."

        }
        if (binding.txtInputEtConfirmpassword.text.isNullOrEmpty()) {
            binding.txtInputEtConfirmpassword.error = "Confirm Password cannot be black."

        }
        if (binding.txtInputEtSetpassword.text == binding.txtInputEtConfirmpassword.text) {
            showToast("Password and Confirm password must be same")

        }
        getOtp("+91"+binding.txtInputEtSetPhone.text.toString())
    }



    private fun doRegister() {
        if (binding.txtInputEtNameSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtNameSignUp.error = "Name cannot be blank."

        }
        else if (binding.txtInputEtPhoneSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPhoneSignUp.error = "Phone no cannot be blank."

        }

        else if (binding.txtInputEtPhoneSignUp.text.toString().isValidPhoneNumber().not()) {
            binding.txtInputEtPhoneSignUp.error = "Enter correct phone number."
        }
        else if (binding.txtInputEtPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtPasswordSignUp.error = "Password cannot be black."

        }
        else if (binding.txtInputEtCnfrmPasswordSignUp.text.isNullOrEmpty()) {
            binding.txtInputEtCnfrmPasswordSignUp.error = "Confirm Password cannot be black."

        }
       else if (binding.txtInputEtCnfrmPasswordSignUp.text == binding.txtInputEtPasswordSignUp.text) {
            showToast("Password and Confirm password must be same")

        }
        else{
            getOtp("+91"+binding.txtInputEtPhoneSignUp.text.toString())
        }

       // startActivity(Intent(this,MainActivity::class.java))


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
            binding.txtInputEtPassword.text.toString(),
            this.applicationContext
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
                    prefsUtil.isLoggedIn = true
                    prefsUtil.Name = binding.txtInputEtPhone.text.toString()
                    prefsUtil.password = binding.txtInputEtPassword.text.toString()
                    var intent=Intent(Intent(this, MainActivity::class.java))
                    intent.putExtra("key",binding.txtInputEtPhone.text.toString())
                    startActivity(intent)
                }
                is AuthResource.Error -> {}
                AuthResource.InvalidPhoneNumber -> {
                    Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_SHORT).show()
                }
                AuthResource.Loading ->{}
                AuthResource.NoUserFound -> {
                    Toast.makeText(this,"No User Found",Toast.LENGTH_SHORT).show()
                }
                AuthResource.OtpRequired -> {}
                AuthResource.OtpSend -> {}
                is AuthResource.VerificationFailed -> {}
                AuthResource.WrongPassword -> {
                    Toast.makeText(this,"wrong Password",Toast.LENGTH_SHORT).show()
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

                    if(binding.txtInputEtNameSignUp.text.isNullOrEmpty()) {
                        flipCard(binding.cvOtp, binding.cvForget) { showToast(it) }

                        Log.d("checkOtp", "" + it)
                    }
                    else{
                        ViewState.Loading
                        flipCard(binding.cvOtp, binding.cvRegister) { showToast(it) }
                        Log.d("checkOtp", "" + it)
                    }
                    showToast("Otp Sent Successfully")
                    ViewState.Success()

                }
                AuthResource.Success -> {
//                    if (binding.matCbRememberMe.isChecked) {
//                        prefsUtil.isLoggedIn = true
//                        prefsUtil.Name = binding.txtInputEtPhoneSignUp.text.toString()
//                        prefsUtil.password = binding.txtInputEtCnfrmPasswordSignUp.text.toString()
//                    }
                    if(binding.txtInputEtNameSignUp.text.isNullOrEmpty()){
                        val ref1=FirebaseDatabase.getInstance().getReference("UserNew")
                        ref1.child(binding.txtInputEtSetPhone.text.toString()).child("password").setValue(binding.txtInputEtSetpassword.text.toString())
                        ref1.child(binding.txtInputEtSetPhone.text.toString()).child("otpVerified").setValue(true)
                        prefsUtil.isLoggedIn = true
                        prefsUtil.Name = binding.txtInputEtSetPhone.text.toString()
                        prefsUtil.password = binding.txtInputEtSetpassword.text.toString()
                        showToast("Password set Successfully")
                    }
                    else {
                        with(binding) {
                            val currentDate1 = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
                                Date()
                            )
                            val currentTime1 = SimpleDateFormat("HHmmss", Locale.getDefault()).format(
                                Date()
                            )
                            var combo = currentDate1 + currentTime1
                            val obj = UserModel(
                                address = null,
                                pincode = null,
                                name = txtInputEtNameSignUp.text.toString(),
                                password = txtInputEtPasswordSignUp.text.toString(),
                                uid = FirebaseAuth.getInstance().uid!!,
                                otpVerified = true,
                                time = combo

                            )
                            val title="New User register"
                            val message="Name:- ${txtInputEtNameSignUp.text.toString()} Phone:- ${txtInputEtPhone.text.toString()}"
                            //val pushNotification= (title,message)
                            obj?.name?.let { it1 -> Log.d("checkObj", it1) }
                            users.child(txtInputEtPhoneSignUp.text.toString()).setValue(obj)
                            showToast("Register Successfully")

                        }
                    }
//                            sendEmail(name.text.toString(), phone.text.toString())
                    var intent=Intent(this, MainActivity::class.java)
                    intent.putExtra("key",binding.txtInputEtNameSignUp.text.toString())
                    startActivity(intent)
                }
                is AuthResource.VerificationFailed -> {
                    Log.d("SAHIL",it.error)
                    showToast(it.error)
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(binding.txtInputEtPhone.text!=null && binding.txtInputEtPhone.text.toString()!="" && binding.txtInputEtPassword.text!=null && binding.txtInputEtPassword.text.toString()!="" &&prefsUtil.Name!=null && prefsUtil.Name.toString()!=""&& prefsUtil.password!=null && prefsUtil.password.toString()!="")
            if(binding.txtInputEtPhone.text.toString()==prefsUtil.Name && binding.txtInputEtPassword.text.toString()==prefsUtil.password)
                startActivity(Intent(this,MainActivity::class.java))
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
