package com.example.networkmodule.usecase

import android.content.Context
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import com.example.networkmodule.repository.ProductRepository
import com.example.networkmodule.util.Util.reduceBase64ImageSize
import com.example.networkmodule.util.computation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class FirebaseGetProductUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
    private val repository:ProductRepository
) {
    operator fun invoke(): Flow<Resource<List<ProductModel>>> = channelFlow {
        send(Resource.Loading())
        firebaseDatabaseRepository.getAllProduct().collect {
            computation {
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    if (list == null) {
                        send(Resource.Error("No Product Found"))
                    } else {
                        val newList=list.map { it.toProductEntity() }
//                        repository.insertItems(newList)
                        send(Resource.Success(list))
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