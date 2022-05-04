package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.ProductDao
import com.example.networkmodule.database.ProductEntity

import com.example.networkmodule.network.FirebaseKey
import com.google.firebase.database.*
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    private val productDao:ProductDao
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<product>>()
    val productList = _productList.toLiveData()

    init {
        getAllProductList()
    }

    private fun getAllProductList() {
        launch {
            _viewState.postValue(ViewState.Loading)
            productDbRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val p = ArrayList<product>()
                    for (dataSnapshot1 in snapshot.children) {
                        val p1 = dataSnapshot1.getValue<product>(product::class.java)
                        if (p1 != null) {
                            p.add(p1)
                        }
                    }
                    Log.d("SAHIL",snapshot.toString())
                    Log.d("SAHIL",p.toString())
                    _productList.postValue(p)
                    _viewState.postValue(ViewState.Success())
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })
        }
    }
    fun insertToDB(productItem:ProductEntity){
        viewModelScope.launch {
            productDao.insertPhone(productItem)
        }
    }

}