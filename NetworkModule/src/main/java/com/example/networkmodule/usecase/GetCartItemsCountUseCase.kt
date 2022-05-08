package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetCartItemsCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() = flow {
        try {
            emit(Resource.Loading())
            val result = cartRepository.getNoOfItemsInCart()
            Log.d("usecase", "" + result)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
        } catch (E: IOException) {
            emit(Resource.Error("Couldn't connect to db"))
        }

    }
}