package com.example.networkmodule.repository

import com.example.networkmodule.database.cart.CartDao
import com.example.networkmodule.database.cart.CartEntity
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) :CartRepository{
    override suspend fun getCartItem(): List<CartEntity> = dao.getProduct()
}