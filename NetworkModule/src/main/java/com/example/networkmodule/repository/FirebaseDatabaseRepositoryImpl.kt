package com.example.networkmodule.repository

import android.util.Log
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.FirebaseKey
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

class FirebaseDatabaseRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.CATEGORY_DATABASE_REF) private val categoryDbRef: DatabaseReference,
) : FirebaseDatabaseRepository {

    override suspend fun getAllProduct() = callbackFlow<Result<List<ProductModel>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<ProductModel>()
                dataSnapshot.children.forEach {
                    it.children.forEach { ittt ->
                        val productLocal = ittt.getValue(ProductModel::class.java)
                        productLocal?.let { it1 ->
                            list.add(it1)
                        }
                    }
                }
                Log.d("SAHIL__", "product " + list.size.toString())
                this@callbackFlow.trySendBlocking(Result.success(list.toList()))
            }
        }
        productDbRef.addValueEventListener(postListener)
        awaitClose {
            productDbRef.removeEventListener(postListener)
        }
    }

    override suspend fun getBanner() = callbackFlow<Result<List<SliderModel>>> {
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = ArrayList<SliderModel>()
                dataSnapshot.children.forEach {
                    it.children.forEach { childSnapShot ->
                        val val1 = childSnapShot.child("img").value.toString()
                        val color = childSnapShot.child("color").value.toString()
                        val obj = SliderModel(val1, color)
                        list.add(obj)
                    }
                }
                Log.d("SAHIL__", "banner " + list.size.toString())
                this@callbackFlow.trySendBlocking(Result.success(list.toList()))
            }
        }
        bannerDbRef.addValueEventListener(postListener)
        awaitClose {
            bannerDbRef.removeEventListener(postListener)
        }
    }

    override suspend fun getCategory(): Flow<Result<List<CategoryModel>>> =
        callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryList = ArrayList<CategoryModel>()
                    snapshot.children.forEach {
                        Log.d("SAHIL", "category $it")
                        val categoryModel = it.getValue(CategoryModel::class.java)
                        Log.d("SAHIL", "category $snapshot")
                        categoryModel?.let { it1 -> categoryList.add(it1) }
                    }
                    Log.d("SAHIL__", "banner " + categoryList.size.toString())
                    this@callbackFlow.trySendBlocking(Result.success(categoryList.toList()))
                }
            }
            categoryDbRef.addValueEventListener(postListener)
            awaitClose {
                categoryDbRef.removeEventListener(postListener)
            }
        }
}