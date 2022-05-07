package com.noor.mystore99.amigrate.ui.main.fragment.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.networkmodule.database.cart.CartDao
import com.example.networkmodule.database.cart.CartEntity
import com.example.networkmodule.database.cart.CartModel
import com.example.networkmodule.network.FirebaseKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.categoryModel
import com.noor.mystore99.sliderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UserViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    @Named(FirebaseKey.CATEGORY_DATABASE_REF) private val categoryDbRef: DatabaseReference,
    private val cartDao: CartDao
) : BaseViewModel() {
    private var _bannerList = MutableLiveData<ArrayList<sliderModel>>()
    val bannerList = _bannerList.toLiveData()
    private var _categoryList = MutableLiveData<ArrayList<categoryModel>>()
    val categoryList = _categoryList.toLiveData()

    init {
        getData()
        getCategory()
    }

    private fun getCategory() {
        launch {
            _viewState.postValue(ViewState.Loading)
            categoryDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryList = ArrayList<categoryModel>()
                    snapshot.children.forEach {
                        Log.d("SAHIL", "category $it")
                        val categoryModel = it.getValue(categoryModel::class.java)
                        Log.d("SAHIL", "category $snapshot")
                        categoryModel?.let { it1 -> categoryList.add(it1) }
                    }
                    Log.d("SAHIL", "category $categoryList")
                    _categoryList.postValue(categoryList)
                    _viewState.postValue(ViewState.Success())
                }

                override fun onCancelled(error: DatabaseError) {
                    _viewState.postValue(ViewState.Error(error.message))
                }

            })
        }

    }

    private fun getData() {
        launch {
            _viewState.postValue(ViewState.Loading)
            bannerDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val p = ArrayList<sliderModel>()
                    for (dataSnapshot1 in snapshot.children) {
                        val val1 = dataSnapshot1.child("img").value.toString()
                        val color = dataSnapshot1.child("color").value.toString()
                        val ob = sliderModel(val1, color)
                        p.add(ob)
                    }
                    _bannerList.postValue(p)
                    _viewState.postValue(ViewState.Success())
                }

                override fun onCancelled(error: DatabaseError) {
                    _viewState.postValue(ViewState.Error(error.message))
                }

            })
        }
    }

     fun insertToCartDb(item:CartEntity) {
         viewModelScope.launch {
             val arr=ArrayList<CartEntity>()
             arr.addAll(cartDao.getProduct())
             arr.add(item)
             cartDao.insertAllProduct(arr)
         }
     }

}