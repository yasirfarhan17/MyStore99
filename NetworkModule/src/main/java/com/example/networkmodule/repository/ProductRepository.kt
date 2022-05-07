package com.example.networkmodule.repository

import com.example.networkmodule.database.product.ProductEntity

interface ProductRepository {
    suspend fun getAllProduct():List<ProductEntity>
}