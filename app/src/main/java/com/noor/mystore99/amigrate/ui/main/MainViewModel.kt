package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.ProductDao
import com.example.networkmodule.database.ProductEntity

import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.ProductUsecase
import com.google.firebase.database.*
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.product
import com.noor.mystore99.productModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    private val productDao:ProductDao,
    private val getProduct:ProductUsecase
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<productModel>>()
    val productList = _productList.toLiveData()

    private var _productFromDB = MutableLiveData<ArrayList<ProductEntity>>()
    val productFromDB = _productFromDB.toLiveData()

    private val _setError = MutableLiveData<String>()
    val setError = _setError.toLiveData()
    val keyyy=ArrayList<String>()
    val p = ArrayList<productModel>()
    val pE = ArrayList<ProductEntity>()
    var index=0

    init {
        getAllProductList()
        getProductFromDB()
    }

    private fun getAllProductList() {
        launch {
            _viewState.postValue(ViewState.Loading)
            productDbRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    val keyy=snapshot.key
                    if (keyy != null) {
                        keyyy.add(keyy)
                    }
                    for (dataSnapshot1 in snapshot.children) {
                        val p1 = dataSnapshot1.getValue<productModel>(productModel::class.java)
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
    fun insertToDB(list: ArrayList<ProductEntity>){
        viewModelScope.launch {
            productDao.insertAllProduct(list)
        }
    }

    fun getProductFromDB(){
        getProduct().onEach {
            _viewState.postValue(ViewState.Loading)
            when (it) {
                is Resource.Success -> {
                    _productFromDB.postValue(it.data as ArrayList<ProductEntity>)
                }
                is Resource.Error -> {
                    _setError.postValue(it.message!!)
                    _viewState.postValue(ViewState.Error())
                }
                is Resource.Loading -> {
                    _viewState.postValue(ViewState.Loading)
                }
            }
        }
//        }.launchIn(viewModelScope + exceptionHandler)
            }
    private fun handleFailure(throwable: Throwable?){
        _viewState.postValue(ViewState.Error(throwable))
        Log.e("Network_Error",throwable.toString())
    }
    private val exceptionHandler = CoroutineExceptionHandler{_,exception ->
        handleFailure(throwable = exception)
    }
}
