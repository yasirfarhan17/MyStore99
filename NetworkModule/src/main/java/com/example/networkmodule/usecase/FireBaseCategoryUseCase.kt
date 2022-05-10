package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FireBaseCategoryRepository
import com.example.networkmodule.util.computation
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FireBaseCategoryUseCase @Inject constructor(
    private val fireBaseCategoryRepository: FireBaseCategoryRepository
) {
    operator fun invoke(productName: String): Flow<Resource<List<ProductModel>>> = channelFlow{

        fireBaseCategoryRepository.getAllCategoryProduct(productName).collectLatest {
            Log.d("checkingValue",""+it.data)
            when(it){
                is Resource.Error -> {
                    trySendBlocking(Resource.Error(it.message.toString()))
                }
                is Resource.Loading -> {
                    trySendBlocking(Resource.Loading())
                }
                is Resource.Success -> {
                    trySendBlocking(Resource.Success(it.data!!))
                }
            }
        }
    }
}