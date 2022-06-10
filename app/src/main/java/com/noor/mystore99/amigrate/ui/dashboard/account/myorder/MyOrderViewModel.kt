package com.noor.mystore99.amigrate.ui.dashboard.account.myorder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.usecase.FireBaseCategoryUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.noor.mystore99.amigrate.base.BaseViewModel
import com.noor.mystore99.amigrate.base.ViewState
import com.noor.mystore99.amigrate.util.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOrderViewModel @Inject constructor(
    private val categoryUseCase: FireBaseCategoryUseCase,
) : BaseViewModel() {



    private var _checkoutOrder = MutableLiveData<ArrayList<checkOutModel>>()
    val checkoutOrder = _checkoutOrder.toLiveData()
    private var _orderId = MutableLiveData<ArrayList<String>>()
    val orderId = _orderId.toLiveData()
    val ref=FirebaseDatabase.getInstance().getReference("orderNew")

    fun getOrder(key:String){
        launch {
            _viewState.postValue(ViewState.Loading)

            Log.d("insideMyOrder","$key")
           ref.child(key).addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       val list=ArrayList<checkOutModel>()
                       snapshot.children.forEach {
                           val item: checkOutModel = it.getValue(checkOutModel::class.java)!!
                           list.add(item)
                           _viewState.postValue(ViewState.Success())
                       }
                       Log.d("insideMyOrder","$snapshot $list")
                       _checkoutOrder.postValue(list)
                   }
                   else{
                       _viewState.postValue(ViewState.Success())

                   }
               }

               override fun onCancelled(error: DatabaseError) {
                   _viewState.postValue(ViewState.Success())
               }

           })

    }
}
}