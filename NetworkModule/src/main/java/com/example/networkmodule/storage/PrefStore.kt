package com.example.networkmodule.storage

import kotlinx.coroutines.flow.Flow

interface PrefStore {
    fun getPhoneNumber(): Flow<String>
    suspend fun setPhoneNumber(phoneNumber: String)
    fun getPassword(): Flow<String?>
    suspend fun setPassword(password: String)
    fun isLoggedIn(): Flow<Boolean>
    suspend fun setLoggedIn(loggedIn: Boolean)
}