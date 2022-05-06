package com.noor.mystore99.amigrate.ui.main.fragment

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.example.networkmodule.network.FirebaseKey
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import com.noor.mystore99.sliderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UserViewModel @Inject constructor(
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
) : BaseViewModel() {
    private var _bannerList = MutableLiveData<ArrayList<sliderModel>>()
    val bannerList = _bannerList.toLiveData()

    init {
        getData()
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
}