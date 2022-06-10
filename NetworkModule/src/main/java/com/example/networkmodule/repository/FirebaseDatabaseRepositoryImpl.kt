package com.example.networkmodule.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.ProductModelNew
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.FirebaseKey
import com.example.networkmodule.util.Util
import com.example.networkmodule.util.Util.decodeToBitmap
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class FirebaseDatabaseRepositoryImpl @Inject constructor(
    @Named(FirebaseKey.PRODUCT_DATABASE_REF) private val productDbRef: DatabaseReference,
    @Named(FirebaseKey.BANNER_DATABASE_REF) private val bannerDbRef: DatabaseReference,
    @Named(FirebaseKey.CATEGORY_DATABASE_REF) private val categoryDbRef: DatabaseReference,
) : FirebaseDatabaseRepository {

    override suspend fun getAllProduct() = callbackFlow<Result<List<ProductModel>>> {
//        val storageReference= FirebaseStorage.getInstance().getReference()
//        var arrNew= ArrayList<ProductModelNew>()
//        val reference=FirebaseDatabase.getInstance().getReference("NewCategoryProduct")
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
//                                val imageBitmap=it1.img?.decodeToBitmap()
//                                val uri= Util.getImageUri(context, imageBitmap!!)
//                                val ref: StorageReference =storageReference.child(System.currentTimeMillis().toString())
//                                ref.putFile(uri!!).addOnSuccessListener(object :
//                                    OnSuccessListener<UploadTask.TaskSnapshot> {
//                                    override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
//                                        p0!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener(object:OnSuccessListener<Uri>{
//                                            override fun onSuccess(p0: Uri?) {
//                                                val url=p0.toString()
//                                                val productModelNew= ProductModelNew(it1.products_name,it1.price,url,it1.quant,it1.hindiName,it1.stock)
//                                                arrNew.add(productModelNew)
//                                                Log.d("insideRepo","$url ${dataSnapshot.value} ${ittt.key}  $arrNew")
//
//                                                reference.child(it.key!!).child(it1.products_name.toString()).setValue(productModelNew)
//                                            }
//
//                                        })
//
//                                            }
//
//                                })
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
                        val val1 = dataSnapshot1.child("img").value.toString()
                        val color = dataSnapshot1.child("color").value.toString()
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
}

class UnknownOrWrongNodeException : IOException("Firebase node doesn't exist")
