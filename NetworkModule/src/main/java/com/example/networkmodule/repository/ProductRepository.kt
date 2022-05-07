package com.example.networkmodule.repository

import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.database.entity.ProductEntity

interface ProductRepository {
    suspend fun getAllProduct():List<ProductEntity>
    suspend fun insertItems(list: List<ProductEntity>)
    suspend fun clearAllProduct()
    suspend fun update(price: String, id: String)
    suspend fun getNoOfProducts(): Int?
}