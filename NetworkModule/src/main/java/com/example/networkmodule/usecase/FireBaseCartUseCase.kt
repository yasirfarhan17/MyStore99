package com.example.networkmodule.usecase

import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import com.example.networkmodule.util.Util.reduceBase64ImageSize
import com.example.networkmodule.util.computation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FireBaseCartUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    operator fun invoke(): Flow<Resource<List<CartEntity>>> = channelFlow {
        send(Resource.Loading())
        firebaseDatabaseRepository.getCart().collect {
            computation {
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    if (list == null) {
                        send(Resource.Error("No Product Found"))
                    } else {
                        withContext(Dispatchers.Main) {
                            send(Resource.Success(list))
                        }
                    }
                } else {
                    send(
                        Resource.Error(
                            it.exceptionOrNull()?.localizedMessage ?: "Something went wrong "
                        )
                    )
                }
            }
        }
    }
}