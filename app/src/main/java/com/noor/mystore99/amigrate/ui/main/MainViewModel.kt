package com.noor.mystore99.amigrate.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.network.firebase.FirebaseKey
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
) : BaseViewModel() {
    private var _productList = MutableLiveData<ArrayList<product>>()
    val productList = _productList.toLiveData()

    init {
        getSurah()
    }

    private fun getSurah() {
        launch {
            _viewState.postValue(ViewState.Loading)
            productDbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
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

                override fun onCancelled(error: DatabaseError) {
                    _viewState.postValue(ViewState.Error(error.toException()))
                }

            })
        }
    }
}