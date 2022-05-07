package com.example.networkmodule.repository

import com.example.networkmodule.database.cart.CartEntity

interface CartRepository {

    suspend fun getCartItem():List<CartEntity>
}