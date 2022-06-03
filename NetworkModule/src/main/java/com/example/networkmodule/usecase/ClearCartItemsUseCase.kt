package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class ClearCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
)
{
    suspend operator fun invoke()= cartRepository.clearCart()
}