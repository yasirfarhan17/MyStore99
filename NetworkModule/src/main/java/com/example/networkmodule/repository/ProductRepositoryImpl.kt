package com.example.networkmodule.repository

import com.example.networkmodule.database.product.ProductDao
import com.example.networkmodule.database.product.ProductEntity
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
):ProductRepository{
    override suspend fun getAllProduct(): List<ProductEntity> = dao.getProduct()
}