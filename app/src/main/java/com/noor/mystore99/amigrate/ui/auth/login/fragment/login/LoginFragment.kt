package com.noor.mystore99.amigrate.ui.auth.login.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
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
    val ref = FirebaseDatabase.getInstance().getReference("User")

    override fun getLayout(): Int = R.layout.fragment_login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        with(binding) {
            checkOut.setOnClickListener {
                startActivity(
                    Intent(
                        it.context,
                        com.noor.mystore99.amigrate.ui.main.MainActivity::class.java
                    )
                )
            }


            verify.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        com.noor.mystore99.amigrate.ui.main.MainActivity::class.java
                    )
                )
                Toast.makeText(it.context, "Please wait", Toast.LENGTH_SHORT).show()
                if (ETPhone.text.toString().isNotEmpty()) {
                    searchPhone(ETPhone.text.toString())
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

    }


}