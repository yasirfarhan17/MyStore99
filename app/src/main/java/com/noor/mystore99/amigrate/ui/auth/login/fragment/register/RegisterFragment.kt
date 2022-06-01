package com.noor.mystore99.amigrate.ui.auth.login.fragment.register

import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.networkmodule.network.AuthResource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.amigrate.ui.auth.AuthManager
import com.noor.mystore99.amigrate.ui.auth.login.LoginActivity
import com.noor.mystore99.databinding.FragmentRegistetBinding
import com.noor.mystore99.first
import java.util.concurrent.TimeUnit

class RegisterFragment : BaseFragment<FragmentRegistetBinding, RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModels()

    private val authManager by lazy { AuthManager(requireActivity()) }

    val auth = FirebaseAuth.getInstance()
    lateinit var code: String
    private var mResendToken: ForceResendingToken? = null

    override fun getViewModelClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayout(): Int = R.layout.fragment_registet

    override fun addObservers() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
//        authManager.events.observe(viewLifecycleOwner) {
//            when (it) {
//                is AuthResource.Error -> showToast(it.error)
//                AuthResource.OtpSend -> {
//                    showToast("Otp hs been sent")
//                }
//                AuthResource.Success -> showToast("Successfully registered")
//                AuthResource.VerificationFailed -> showToast("Verification Failed")
//                else -> Log.d("SAHIL", "failed")
//            }
//        }
    }

    private fun initUi() {
        binding.veryfy.setOnClickListener {
          //  authManager.verifyOtp(authManager.mVerificationId)
        }
        binding.checkOut.setOnClickListener {

            with(binding){
                if (etPhone.text.isNullOrEmpty()) {
                    showMessage("Phone Number cannot be blank")
                    etPhone.error="required"

                }
                if (etPassword.text.isNullOrEmpty()) {
                    showMessage("Password cannot be blank")
                    etPassword.error="required"


                }
                if (etCnfrmPassword.text.isNullOrEmpty()) {
                    showMessage("Confirm Password cannot be blank")
                    etPassword.error="required"

                }
                if (etCnfrmPassword.text == etPassword.text) {
                    showMessage("Password and Confirm password must be same")
                    etPassword.error="required"
                }
                if (etPhone.text?.length!! < 10) {
                    etPhone.setError("Enter valid phone number")
                    etPhone.requestFocus()
                }
                else{
                    register(
                        binding.etPhone.text.toString(),
                        binding.etPassword.text.toString(),
                    )

                    val transition1: Transition = Slide(Gravity.RIGHT)
                    transition1.setDuration(800)
                    transition1.addTarget(veryfy)
                    transition1.addTarget(textOTP)
                    transition1.addTarget(linearLayoutCompat)



                    TransitionManager.beginDelayedTransition(binding.root as ViewGroup?, transition1)
                    veryfy.setVisibility(View.VISIBLE)
                    linearLayoutCompat.setVisibility(View.VISIBLE)
                    textOTP.setVisibility(View.VISIBLE)


                    val transition: Transition = Slide(Gravity.LEFT)
                    transition.setDuration(600)
                    transition.addTarget(textLayout)
                    transition.addTarget(textLayout1)
                    transition.addTarget(textLayout3)
                    transition.addTarget(checkOut)

                    TransitionManager.beginDelayedTransition(binding.root as ViewGroup?, transition)
                    textLayout.setVisibility(View.GONE)
                    textLayout1.setVisibility(View.GONE)
                    textLayout3.setVisibility(View.GONE)
                    checkOut.setVisibility(View.GONE)
                }




            }

        }
    }

    fun register(phone: String, etPassword: String) {

        val newMobile="+91"+phone

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(newMobile)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this.requireActivity())
            .setCallbacks(mCallBacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        Toast.makeText(activity,"hiiii",Toast.LENGTH_SHORT).show();

        //authManager.sendOtp(mobile = phone, false)





    }


    fun verifySignInCode(codeEdit: String) {
        val credential = PhoneAuthProvider.getCredential(code, codeEdit)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,

    ) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this.requireActivity(),
                    OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
//                            val obj = User(name.text.toString(), phone.text.toString())
//                            users.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(obj)
//                            sendEmail(name.text.toString(), phone.text.toString())
                            Toast.makeText(
                                activity,
                                "Registration Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            // SmsManager smsManager = SmsManager.getDefault();
                            //smsManager.sendTextMessage(phone.getText().toString(), null, String.valueOf(number)+" is your verification code for Sabzi Taza", null, null);
                            //Toast.makeText(getApplicationContext(), "SMS Sent!",
                            //      Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "Incorrect code", Toast.LENGTH_SHORT).show()
                        }
                    })
        }


    var mCallBacks:OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code = p0.smsCode
//                name = findViewById<EditText>(R.id.name)
//                phone = findViewById<EditText>(R.id.LoginPhone)


            // In case OTP is received
            if (code != null) {
                //otp.setText(code)
                verifySignInCode(code)
            } else {
                // In case OTP is not received
                signInWithPhoneAuthCredential(p0)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {

        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
        }

        override fun onCodeSent(p0: String, p1: ForceResendingToken) {
            super.onCodeSent(p0, p1)
            mResendToken = p1
            code = p0
            Log.d("checkcode",p0)
            Toast.makeText(activity, "OTP send successfully!!", Toast.LENGTH_SHORT)
                .show()
        }
    }


//    var mCallbacks: OnVerificationStateChangedCallbacks =
//        object : OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                val code = phoneAuthCredential.smsCode
////                name = findViewById<EditText>(R.id.name)
////                phone = findViewById<EditText>(R.id.LoginPhone)
//
//
//                // In case OTP is received
//                if (code != null) {
//                    //otp.setText(code)
//                    verifySignInCode(code)
//                } else {
//                    // In case OTP is not received
//                    signInWithPhoneAuthCredential(phoneAuthCredential)
//                }
//            }
//
//            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
//                super.onCodeSent(s, forceResendingToken)
//                mResendToken = forceResendingToken
//                code = s
//                Toast.makeText(activity, "OTP send successfully!!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {}
//        }
}
