package com.noor.mystore99.amigrate.ui.cart

import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.network.FirebaseKey
import com.google.firebase.database.DatabaseReference
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.productModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CartViewModel @Inject constructor(
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<productModel>>()
    val productList = _productList.toLiveData()


}
