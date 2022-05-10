package com.example.networkmodule.repository

import android.util.Log
import com.example.networkmodule.model.ProductModel
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

class FireBaseCategoryRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val categoryRef:DatabaseReference
) : FireBaseCategoryRepository{
    override suspend fun getAllCategoryProduct(productName: String): Flow<Resource<List<ProductModel>>> = callbackFlow<Resource<List<ProductModel>>> {

        trySendBlocking(Resource.Loading())
        val postList=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list=ArrayList<ProductModel>()
                snapshot.children.forEach {
                        val productlocal = it.getValue(ProductModel::class.java)
                        productlocal?.let {i ->
                            list.add(i)
                        }
                    }
                    Log.d("productC", "product " + list)

                    this@callbackFlow.trySendBlocking(Resource.Success(list.toList()))
                }


            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Resource.Error(error.message))
            }


        }
        categoryRef.child(productName).addValueEventListener(postList)
        awaitClose {
            categoryRef.removeEventListener(postList)
        }
    }
}