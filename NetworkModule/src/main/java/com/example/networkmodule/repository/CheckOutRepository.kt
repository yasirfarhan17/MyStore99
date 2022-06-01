package com.example.networkmodule.repository

import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import kotlinx.coroutines.flow.Flow


interface CheckOutRepository {

    suspend fun getOrderDetail(key:String,orderId:String) : Flow<Result<checkOutModel>>
}