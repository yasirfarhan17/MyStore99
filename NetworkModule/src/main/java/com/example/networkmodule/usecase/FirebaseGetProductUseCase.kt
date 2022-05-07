package com.example.networkmodule.usecase

import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseGetProductUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    operator fun invoke(): Flow<Resource<List<ProductModel>>> = flow {
        emit(Resource.Loading())
        firebaseDatabaseRepository.getAllProduct().collect {
            if (it.isSuccess) {
                val list = it.getOrNull()
                if (list == null) {
                    emit(Resource.Error("No Product Found"))
                } else {
                    emit(Resource.Success(list))
                }
            } else {
                emit(
                    Resource.Error(
                        it.exceptionOrNull()?.localizedMessage ?: "Something went wrong "
                    )
                )
            }
        }
    }

}