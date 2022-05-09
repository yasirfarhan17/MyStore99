package com.example.networkmodule.repository

import com.example.networkmodule.network.AuthResource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Flow<AuthResource>
}