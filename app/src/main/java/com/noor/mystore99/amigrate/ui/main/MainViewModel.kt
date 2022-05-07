package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.product.ProductDao
import com.example.networkmodule.database.product.ProductEntity
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.ProductUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import com.example.networkmodule.database.product.productModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    private val productDao: ProductDao,
    private val getProduct: ProductUseCase
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<productModel>>()
    val productList = _productList.toLiveData()

    private var _productFromDB = MutableLiveData<ArrayList<ProductEntity>>()
    val productFromDB = _productFromDB.toLiveData()

    init {
        getAllProductList()
        getProductFromDB()
    }


    private fun getAllProductList() {
        launch {
            _viewState.postValue(ViewState.Loading)
            productDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = productList.value ?: ArrayList()
                    list.clear()
                    snapshot.children.forEach {
                        it.children.forEach { ittt ->
                            val productLocal = ittt.getValue(productModel::class.java)
                            productLocal?.let { it1 -> list.add(it1) }
                        }
                    }
                    Log.d("SAHIL__", list.size.toString())
                    _productList.postValue(list)
                    _viewState.postValue(ViewState.Success())
                }

                override fun onCancelled(error: DatabaseError) {}
            })
/*
            productDbRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val list = productList.value?: ArrayList()
                    list.clear()
                    snapshot.children.forEach {
                        val productLocal = it.getValue(productModel::class.java)
                        productLocal?.let { it1 -> list.add(it1) }
                    }
                    val entityList = list.map { it.toProductEntity() }
                    Log.d("SAHIL__",list.size.toString() +" "+ entityList.size.toString())
                    insertToDB(entityList as ArrayList<ProductEntity>)
                    _viewState.postValue(ViewState.Success())
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
*/
        }
    }

    fun insertToDB(list: ArrayList<ProductEntity>) {
        viewModelScope.launch {
            productDao.insertAllProduct(list)
        }
    }

    fun getProductFromDB() {
        launch {
            getProduct().onEach {
                _viewState.postValue(ViewState.Loading)
                when (it) {
                    is Resource.Success -> {
                        _productFromDB.postValue(it.data as ArrayList<ProductEntity>)
                    }
                    is Resource.Error -> {
                        _viewState.postValue(ViewState.Error(it.message))
                    }
                    is Resource.Loading -> {
                        _viewState.postValue(ViewState.Loading)
                    }
                }
            }
        }
    }
}
