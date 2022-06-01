package com.example.networkmodule.repository

import android.util.Log
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.network.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named

class CheckOutRepositoryImpl @Inject  constructor(
    @Named(FirebaseKey.ORDER_DATABASE_REF) private val orderDbRef: DatabaseReference,
):CheckOutRepository{
    override suspend fun getOrderDetail(key:String,orderId:String) = callbackFlow<Result<checkOutModel>> {
        val postListener = object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    Log.d("inRepo", "$snapshot $key $orderId")
                    var item: checkOutModel = snapshot.getValue(checkOutModel::class.java)!!
                    this@callbackFlow.trySendBlocking(Result.success(item))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

        }
        orderDbRef.child(key).child(orderId).addValueEventListener(postListener)
        awaitClose {
            orderDbRef.child(key).child(orderId).removeEventListener(postListener)
        }
    }
}