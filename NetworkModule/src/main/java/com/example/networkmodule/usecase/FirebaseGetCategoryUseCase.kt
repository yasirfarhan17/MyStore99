package com.example.networkmodule.usecase

import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseGetCategoryUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    operator fun invoke(): Flow<Resource<List<CategoryModel>>> = flow {
        emit(Resource.Loading())
        firebaseDatabaseRepository.getCategory().collect {
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