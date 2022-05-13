package com.example.networkmodule.repository

import com.example.networkmodule.database.dao.ProductDao
import com.example.networkmodule.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
) : ProductRepository {
    override suspend fun getAllProduct(): Flow<List<ProductEntity>> = dao.getProduct()
    override suspend fun insertItems(list: List<ProductEntity>) = dao.insertAllProduct(list)
    override suspend fun clearAllProduct() = dao.clear()
    override suspend fun update(price: String, id: String) = dao.update(price, id)
    override suspend fun updateCount(count: Int, id: String) = dao.updateCount(count, id)
    override suspend fun getNoOfProducts(): Int? = dao.getCount()
}