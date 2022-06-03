package com.example.networkmodule.repository

import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.model.SliderModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDatabaseRepository {
    suspend fun getAllProduct(): Flow<Result<List<ProductModel>>>
    suspend fun getAllProductCount(): Flow<Result<Long>>
    suspend fun getBanner(): Flow<Result<List<SliderModel>>>
    suspend fun getCategory(): Flow<Result<List<CategoryModel>>>
    suspend fun getAllCategoryProduct(productName: String): Flow<Result<List<ProductModel>>>

}