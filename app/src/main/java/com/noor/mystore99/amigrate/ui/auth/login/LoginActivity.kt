package com.noor.mystore99.amigrate.ui.auth.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, MainLoginViewModel>() {

    override val viewModel: MainLoginViewModel by viewModels()


    override fun layoutId(): Int = R.layout.activity_login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavView()

    }


    private fun setNavView() {
        val navView: BottomNavigationView = binding.bottomNavigationView1
        val navController = findNavController(R.id.nav_host_fragment_activity_Login)

        navView.setupWithNavController(navController)

    }


    override fun addObservers() {

    }
}