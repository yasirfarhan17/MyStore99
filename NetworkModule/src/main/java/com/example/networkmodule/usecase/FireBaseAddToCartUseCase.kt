package com.example.networkmodule.usecase

import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class FireBaseAddToCartUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    operator fun invoke(cartItemList: ArrayList<CartEntity>): Flow<Resource<String>> = channelFlow {
        send(Resource.Loading())
        firebaseDatabaseRepository.addItemToCart(cartItemList = cartItemList).collectLatest {
            if (it.isSuccess) {
                val res = it.getOrNull()
                if (res == null) {
                    send(Resource.Error("Something went wrong"))
                } else {
                    send(Resource.Success(res))
                }
            } else {
                send(Resource.Error(it.exceptionOrNull()?.localizedMessage.toString()))
            }
        }

    }
}