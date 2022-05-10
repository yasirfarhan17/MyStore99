package com.example.networkmodule.repository

import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import kotlinx.coroutines.flow.Flow

interface FireBaseCategoryRepository {
    suspend fun getAllCategoryProduct(productName:String): Flow<Resource<List<ProductModel>>>
}