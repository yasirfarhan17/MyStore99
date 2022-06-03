package com.example.networkmodule.repository

import com.example.networkmodule.database.dao.CartDao
import com.example.networkmodule.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) : CartRepository {
    override suspend fun getCartItem(): List<CartEntity> = dao.getAllCartProduct()
    override suspend fun insertItemToCart(list: List<CartEntity>) = dao.insertAllCartProduct(list)
    override suspend fun clearCart() = dao.clear()
    override suspend fun update(price: String, id: String) = dao.update(price, id)
    override suspend fun updateCount(count: Int, id: String) = dao.updateCount(count, id)
    override suspend fun getNoOfItemsInCart(): Flow<Int?> = dao.getCount()
    override suspend fun deleteItem(id: String) = dao.deleteItem(id)
}