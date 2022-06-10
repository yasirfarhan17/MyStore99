package com.example.networkmodule.repository

import android.util.Log
import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.FirebaseKey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named

class MYOrderImpl @Inject constructor(
    @Named(FirebaseKey.ORDER_DATABASE_REF) private val orderDbRef: DatabaseReference,
):MyOrderRepo{
    override suspend fun getOrderDetail(key:String) = callbackFlow<Result<List<checkOutModel>>> {
        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    //Log.d("inRepo", "$snapshot $key $orderId")
                        val list=ArrayList<checkOutModel>()
                        snapshot.children.forEach {
                            var item: checkOutModel = it.getValue(checkOutModel::class.java)!!
                            list.add(item)

                        }
                    this@callbackFlow.trySendBlocking(Result.success(list))

                }

            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

        }
        orderDbRef.child(key).addValueEventListener(postListener)
        awaitClose {
            orderDbRef.child(key).removeEventListener(postListener)
        }
    }
}