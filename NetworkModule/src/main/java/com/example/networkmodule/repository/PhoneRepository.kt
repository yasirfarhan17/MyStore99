package com.example.networkmodule.repository

import com.example.networkmodule.model.ProductsItem
import com.example.networkmodule.model.Response

interface PhoneRepository {
    suspend fun getPhoneData():Response
}