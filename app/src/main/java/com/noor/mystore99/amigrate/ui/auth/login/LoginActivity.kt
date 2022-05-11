package com.noor.mystore99.amigrate.ui.auth.login

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.activity.viewModels
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
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
        val scale = applicationContext.resources.displayMetrics.density


        binding.cvLogin.cameraDistance = 8000 * scale
        binding.cvRegister.cameraDistance = 8000 * scale


        // Now we will set the event listener
        binding.tvSignUp.setOnClickListener {/*
            frontAnim.setTarget(binding.cvLogin);
            backAnim.setTarget(binding.cvRegister);
            frontAnim.start()
            backAnim.start()*/
            flipCard(binding.cvRegister, binding.cvLogin) {
                showMessage(it)
            }

        }
        binding.tvSignIn.setOnClickListener {
            /* frontAnim.setTarget(binding.cvRegister)
             backAnim.setTarget(binding.cvLogin)
             backAnim.start()
             frontAnim.start()*/
            flipCard(binding.cvLogin, binding.cvRegister) {
                showMessage(it)

            }

        }
    }


    private fun initUi() {

    }

    private fun addListener() {

    }


    override fun addObservers() {

    }
}
