package com.noor.mystore99.amigrate.ui.dashboard.account.address

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityAddressBinding

class Address : BaseActivity<ActivityAddressBinding,AdressViewModel>() {

    override val viewModel: AdressViewModel by viewModels()

    val user=FirebaseDatabase.getInstance().getReference("UserNew").child("9163626276")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_address)

        binding.submit.setOnClickListener {
            getInit()
        }

    }

    fun getInit(){
        with(binding){

            if(txtInputEtAdress.text.toString().isEmpty()  || txtInputEtAdress.text==null){
                txtInputEtAdress.setError("Required")
            }
            if(txtInputEtCity.text.toString().isEmpty() || txtInputEtCity.text ==null){
                        txtInputEtCity.setError("Required")
                    }
            if(txtInputEtPincode.text.toString().isEmpty()  || txtInputEtPincode.text==null){
                                txtInputEtPincode.setError("Required")
            }
               else {
                   val address = txtInputEtAdress.text.toString()+ " "+ txtInputEtCity.text.toString()

                user.child("address").setValue(address)
                user.child("pincode").setValue(txtInputEtPincode.text.toString())



            }

        }
    }



    override fun layoutId(): Int =R.layout.activity_address

    override fun addObservers() {

    }
}