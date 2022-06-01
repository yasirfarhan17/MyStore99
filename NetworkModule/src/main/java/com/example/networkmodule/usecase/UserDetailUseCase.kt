package com.example.networkmodule.usecase

import com.example.networkmodule.model.UserModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.UserDetail
import com.example.networkmodule.repository.UserDetailImpl
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserDetailUseCase @Inject constructor(
    private val userRepository: UserDetail
) {
    operator fun invoke(key: String): Flow<Resource<UserModel>> = flow {
        emit(Resource.Loading())
        userRepository.getUserDetail(key).collect {
            if (it.isSuccess) {
                if (it.getOrNull() == null) {
                    emit(Resource.Error("No User Detail Found"))
                } else {
                    emit(Resource.Success(it.getOrNull()!!))
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