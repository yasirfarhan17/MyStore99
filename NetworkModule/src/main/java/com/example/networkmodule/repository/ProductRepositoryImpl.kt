package com.example.networkmodule.repository

import com.example.networkmodule.database.ProductDao
import com.example.networkmodule.database.ProductEntity
import com.noor.mystore99.productModel
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
):ProductRepository{
    override suspend fun getAllProduct(): List<ProductEntity> = dao.getProduct()
}