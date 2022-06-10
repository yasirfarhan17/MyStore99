package com.example.networkmodule.repository

import com.example.networkmodule.model.checkOutModel
import kotlinx.coroutines.flow.Flow

interface MyOrderRepo {
    suspend fun getOrderDetail(key:String) : Flow<Result<List<checkOutModel>>>
}