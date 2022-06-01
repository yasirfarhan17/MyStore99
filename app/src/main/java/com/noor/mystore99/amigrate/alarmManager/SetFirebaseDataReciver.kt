package com.noor.mystore99.amigrate.alarmManager//package com.noor.mystore99.amigrate.alarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FirebaseGetProductUseCase
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.ui.main.MainActivity
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserFragment
import com.noor.mystore99.amigrate.ui.main.fragment.home.UserViewModel
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
 class SetFirebaseDataReciver : BroadcastReceiver(){



    @Inject
    lateinit var productUseCase: FirebaseGetProductUseCase
    private var _productList = MutableLiveData<ArrayList<ProductModel>>()
    val productList = _productList.toLiveData()
    override fun onReceive(p0: Context?, p1: Intent?) {

        getAllProducts()
        Toast.makeText(p0,"InsideBroadCast",Toast.LENGTH_SHORT).show()
    }

    private fun getAllProducts () {

        GlobalScope.launch(Dispatchers.IO) {
            productUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        _productList.postValue(it.data as ArrayList<ProductModel>)
                        //_viewState.postValue(ViewState.Success())
                    }
                    is Resource.Error -> {
                        // _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        //_viewState.postValue(ViewState.Loading)
                    }
                }
            }
        }

    }
}