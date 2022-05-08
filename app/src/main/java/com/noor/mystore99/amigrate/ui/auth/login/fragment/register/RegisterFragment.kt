package com.noor.mystore99.amigrate.ui.auth.login.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.databinding.ActivityLoginBinding
import com.noor.mystore99.databinding.FragmentRegistetBinding

class RegisterFragment : BaseFragment<FragmentRegistetBinding,RegisterViewModel>() {
    override val viewModel: RegisterViewModel by viewModels()


    override fun getViewModelClass(): Class<RegisterViewModel> =RegisterViewModel::class.java

    override fun getLayout(): Int =R.layout.fragment_registet

    override fun addObservers() {
    }


}
