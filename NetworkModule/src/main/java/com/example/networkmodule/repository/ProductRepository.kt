package com.example.networkmodule.repository

import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.productModel

interface ProductRepository {
    suspend fun getAllProduct():List<ProductEntity>
}