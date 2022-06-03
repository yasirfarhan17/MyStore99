package com.example.networkmodule.usecase

import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.repository.CartRepository
import javax.inject.Inject

class InsertCartItemUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(list: ArrayList<CartEntity>) = repository.insertItemToCart(list)
}