package com.noor.mystore99.amigrate.ui.dashboard.account.address

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.base.BaseActivity
import com.noor.mystore99.databinding.ActivityAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Address : BaseActivity<ActivityAddressBinding,AdressViewModel>() {

    override val viewModel: AdressViewModel by viewModels()
    lateinit var  key:String

    var user=FirebaseDatabase.getInstance().getReference("UserNew")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_address)
        key= prefsUtil.Name.toString()
        user=user.child(key)

        binding.submit.setOnClickListener {
            getInit()
        }

    }

    fun getInit(){
        with(binding){

            if(txtInputEtAdress.text.toString().isEmpty()  || txtInputEtAdress.text==null){
                txtInputEtAdress.setError("Required")
            }
            if(txtInputEtLandmark.text.toString().isEmpty()  || txtInputEtLandmark.text==null){
                txtInputEtAdress.setError("Required")
            }
            if(txtInputEtCity.text.toString().isEmpty() || txtInputEtCity.text ==null){
                        txtInputEtCity.setError("Required")
                    }
            if(txtInputEtPincode.text.toString().isEmpty()  || txtInputEtPincode.text==null){
                                txtInputEtPincode.setError("Required")
            }
               else {
                   val address = txtInputEtAdress.text.toString()+",  "+ txtInputEtCity.text.toString() +" land Marks:- "+txtInputEtLandmark.text.toString()

                user.child("address").setValue(address)
                user.child("pincode").setValue(txtInputEtPincode.text.toString())
                Toast.makeText(this@Address,"Address updated successfully", Toast.LENGTH_SHORT).show()
                onBackPressed()
                txtInputEtAdress.text?.clear()
                txtInputEtLandmark.text?.clear()
                txtInputEtCity.text?.clear()
                txtInputEtPincode.text?.clear()

            }

        }
    }



    override fun layoutId(): Int =R.layout.activity_address

    override fun addObservers() {

    }
}