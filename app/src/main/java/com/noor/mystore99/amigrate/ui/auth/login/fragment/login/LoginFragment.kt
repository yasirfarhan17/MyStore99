package com.noor.mystore99.amigrate.ui.auth.login.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.networkmodule.network.AuthResource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseFragment
import com.noor.mystore99.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, loginViewModel>() {

    override val viewModel: loginViewModel by viewModels()
    var flag: Boolean = false

    override fun getViewModelClass(): Class<loginViewModel> = loginViewModel::class.java
    var ref = FirebaseDatabase.getInstance().getReference("User")

    override fun getLayout(): Int = R.layout.fragment_login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        with(binding) {
            checkOut.setOnClickListener {
                viewModel.doLogin(binding.etPhone.text.toString(), "123")
            }

            verify.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        com.noor.mystore99.amigrate.ui.main.MainActivity::class.java
                    )
                )
                Toast.makeText(it.context, "Please wait", Toast.LENGTH_SHORT).show()
                if (etPhone.text.toString().isNotEmpty()) {
                    searchPhone(etPhone.text.toString())
                }
            }


        }
    }

    fun searchPhone(phone: String) {
        GlobalScope.launch {
            Log.d("check", "inside " + phone)
            ref.orderByChild("phone").equalTo(phone)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            flag = true
                            binding.textLayout1.visibility = View.INVISIBLE
                            binding.linearLayoutCompat.visibility = View.VISIBLE
                            binding.otp.visibility = View.VISIBLE
                            binding.checkOut.visibility = View.VISIBLE
                            Log.d("check", "working")
                        } else {
                            Log.d("check", "working1")
                            flag = false
                            binding.textLayout1.visibility = View.VISIBLE
                            binding.otp.visibility = View.INVISIBLE
                            binding.linearLayoutCompat.visibility = View.INVISIBLE
                            binding.checkOut.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        }

    }

    override fun addObservers() {
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is AuthResource.Error -> showToast(it.error)
                AuthResource.InvalidPhoneNumber -> showToast("Invalid Phone Number")
                AuthResource.Loading -> showProgress()
                AuthResource.NoUserFound -> showToast("No User FOund")
                AuthResource.OtpRequired -> showToast("otp required")
                AuthResource.Success -> {
                    hideProgress()
                    showToast("Successful")
                }
                AuthResource.VerificationFailed -> showToast("Verification failed")
                AuthResource.WrongPassword -> showToast("wrog pass word failed")
                else -> showToast("error")
            }
        }
    }


}