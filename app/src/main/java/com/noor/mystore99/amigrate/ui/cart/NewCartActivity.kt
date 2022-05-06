package com.noor.mystore99.amigrate.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.noor.mystore99.R
import com.noor.mystore99.databinding.ActivityNewCartBinding

class NewCartActivity : AppCompatActivity() {

    lateinit var binding:ActivityNewCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_new_cart)
    }


    fun initUi(){
        with(binding){
            CartRecyclerView3.layoutManager= LinearLayoutManager(this@NewCartActivity)
            //CartRecyclerView3.adapter=CartAdapter(this@NewCartActivity)
        }
    }
}