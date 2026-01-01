package com.example.networkmodule.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.*
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.storage.PrefsUtil
import com.example.networkmodule.util.Util
import com.example.networkmodule.util.Util.decodeToBitmap
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseException
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named


class FirebaseDatabaseRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.CATEGORY_DATABASE_REF) private val categoryDbRef: DatabaseReference,
    @Named(FirebaseKey.CART_DATABASE_REF) private val cartDbRef: DatabaseReference,
    private val prefsUtil: PrefsUtil
) : FirebaseDatabaseRepository {
//    var storageReference:StorageReference=FirebaseStorage.getInstance().reference
//    var reference=FirebaseDatabase.getInstance().getReference("NewCategoryProduct")
//    var arrNew=ArrayList<ProductModelNew>();
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
                val count = dataSnapshot.childrenCount.toInt()
                for (i in 0 until count + 1) {
                    for (dataSnapshot1 in dataSnapshot.children) {
                        val val1 = dataSnapshot1.child("banner").value.toString()
                        val color = dataSnapshot1.child("backgroundColor").value.toString()
                        val ob = SliderModel(val1, color)
                        list.add(ob)
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

    override suspend fun getAllCategoryProduct(productName: String): Flow<Result<List<ProductModel>>> =
        callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()
                            .not() || dataSnapshot.key == null || dataSnapshot.value == null
                    ) {
                        this@callbackFlow.trySendBlocking(Result.failure(UnknownOrWrongNodeException()))
                        return
                    }
                    val list = ArrayList<ProductModel>()
                    dataSnapshot.children.forEach {
                        val productLocal = it.getValue(ProductModel::class.java)
                        productLocal?.let { it1 ->
                            list.add(it1)
                        }
                    }
                    Log.d("SAHIL__", "product " + list.size.toString())
                    this@callbackFlow.trySendBlocking(Result.success(list.toList()))
                }
            }
            productDbRef.child(productName).addValueEventListener(postListener)
            awaitClose {
                productDbRef.removeEventListener(postListener)
            }
        }

    override suspend fun addItemToCart(cartItemList: CartEntity): Flow<Result<String>> =
        flow {
            cartDbRef.child(prefsUtil.Name!!).child(cartItemList.products_name)
                .setValue(cartItemList)
            emit(Result.success("Item Added Successfully"))
        }

    override suspend fun updateCart(
        price: String,
        id: String,
        quant: String
    ): Flow<Result<String>> =
        flow {
            cartDbRef.child(prefsUtil.Name!!).child(id).child("quant")
                .setValue(quant)
            cartDbRef.child(prefsUtil.Name!!).child(id).child("total")
                .setValue(price)
            emit(Result.success("update successfully"))
        }


    override suspend fun getCart(): Flow<Result<List<CartEntity>>> =
        callbackFlow {
            val postListener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val cartList = ArrayList<CartEntity>()
                    snapshot.children.forEach {
                        Log.d("SAHIL_CART", "cart $it")
                        val cartItem = it.getValue(CartModel::class.java)?.toCartEntity()
                        Log.d("SAHIL_CART", "cart $snapshot")
                        cartItem.let { it1 ->
                            if (it1 != null) {
                                cartList.add(it1)
                            }
                        }
                    }
                    this@callbackFlow.trySendBlocking(Result.success(cartList.toList()))
                }
            }
            cartDbRef.child(prefsUtil.Name!!).addValueEventListener(postListener)
            awaitClose {
                cartDbRef.child(prefsUtil.Name!!).removeEventListener(postListener)
            }
        }

    override suspend fun deleteItemFromCart(cartEntity: CartEntity): Flow<Result<String>> =
        flow {
            cartDbRef.child(prefsUtil.Name!!).child(cartEntity.products_name).ref.removeValue()
            emit(Result.success("Item Remove Successfully"))
        }

}

class UnknownOrWrongNodeException : IOException("Firebase node doesn't exist")
class FirebaseSomethingWentWrong : FirebaseException("Something went wrong")
