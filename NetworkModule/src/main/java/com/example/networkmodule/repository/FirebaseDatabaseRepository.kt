package com.example.networkmodule.repository

import android.content.Context
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import com.example.networkmodule.network.Resource
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface FirebaseDatabaseRepository {
    suspend fun getAllProduct(): Flow<Result<List<ProductModel>>>
    suspend fun getBanner(): Flow<Result<List<SliderModel>>>
    suspend fun getCategory(): Flow<Result<List<CategoryModel>>>
    suspend fun getAllCategoryProduct(productName: String): Flow<Result<List<ProductModel>>>
    suspend fun addItemToCart(cartItemList: CartEntity): Flow<Result<String>>
    suspend fun updateCart(price: String, id: String,quant:String): Flow<Result<String>>
    suspend fun getCart(): Flow<Result<List<CartEntity>>>
    suspend fun deleteItemFromCart(cartEntity: CartEntity) : Flow<Result<String>>

}