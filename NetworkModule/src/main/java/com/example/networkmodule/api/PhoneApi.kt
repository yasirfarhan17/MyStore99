package com.example.networkmodule.api

import com.example.networkmodule.model.ProductsItem
import com.example.networkmodule.model.Response
import retrofit2.http.GET

interface PhoneApi {

    @GET(EndPoint.Get_Phone)
    suspend fun getPhone():Response
}