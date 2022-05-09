package com.example.networkmodule.usecase

import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(phoneNumber: String, password: String): Flow<AuthResource> = flow {
        emit(AuthResource.Loading)
        val repo = repository.login(phoneNumber, password)
        repo.collectLatest {
            emit(it)
        }
    }
}