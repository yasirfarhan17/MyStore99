package com.example.networkmodule.repository

import com.example.networkmodule.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCartItem(): List<CartEntity>
    suspend fun insertItemToCart(list: List<CartEntity>)
    suspend fun clearCart()
    suspend fun update(price: String, id: String)
    suspend fun getNoOfItemsInCart():Flow<Int?>
}