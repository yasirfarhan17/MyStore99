package com.example.networkmodule.repository

import com.example.networkmodule.api.PhoneApi
import com.example.networkmodule.model.ProductsItem
import com.example.networkmodule.model.Response
import javax.inject.Inject

class PhoneRepositoryImpl @Inject constructor(
    private val api:PhoneApi
) :PhoneRepository{
    override suspend fun getPhoneData(): Response =api.getPhone()
}