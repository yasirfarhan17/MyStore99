package com.example.networkmodule.repository

import com.example.networkmodule.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserDetail {

    suspend fun getUserDetail(key:String): Flow<Result<UserModel>>
}