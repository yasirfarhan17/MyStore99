package com.example.networkmodule.repository

import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.network.Resource
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Flow<Resource<DataSnapshot>>
}