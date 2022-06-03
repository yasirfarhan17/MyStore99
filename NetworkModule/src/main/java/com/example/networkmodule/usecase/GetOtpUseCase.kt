package com.example.networkmodule.usecase

import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthResource> = flow {

    }
}