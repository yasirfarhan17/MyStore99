package com.noor.mystore99.amigrate.ui.auth

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.AuthResource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AuthManager @Inject constructor(
    var activity: Activity,
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken
    var mVerificationId: String = ""

    private val _events: MutableLiveData<AuthResource> by lazy { MutableLiveData<AuthResource>() }
    val events: LiveData<AuthResource> = _events


    fun sendOtp(mobile: String, resend: Boolean) {
        if (resend) resend(mobile)
        else send(mobile)
    }

    fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(
            mVerificationId,
            otp
        )
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity) {
            _events.value =
                if (it.isSuccessful) AuthResource.Success else AuthResource.Error(it.exception?.localizedMessage.toString())
        }
    }


    private fun send(mobile: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mobile)
            .setTimeout(5L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Toast.makeText(activity.applicationContext, options.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun resend(mobile: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(mobile)
            .setTimeout(5L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallbacks)
            .setForceResendingToken(mResendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(authCredential: PhoneAuthCredential) {
            _events.value = AuthResource.Success
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _events.value = AuthResource.VerificationFailed(e.message.toString())
        }

        override fun onCodeSent(
            verificationId: String,
            forceResendingToken: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, forceResendingToken)
            mResendToken = forceResendingToken
            mVerificationId = verificationId
            _events.value = AuthResource.OtpSend
        }

    }
}