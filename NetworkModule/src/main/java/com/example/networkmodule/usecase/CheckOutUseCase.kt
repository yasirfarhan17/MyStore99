package com.example.networkmodule.usecase

import com.example.networkmodule.model.checkOutModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CheckOutRepository
import com.example.networkmodule.util.computation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectIndexed

import javax.inject.Inject

class CheckOutUseCase @Inject constructor(
    private val repository: CheckOutRepository
) {
    operator fun invoke(key:String,id:String): Flow<Resource<checkOutModel>> = channelFlow {
        computation {
            send(Resource.Loading())
            repository.getOrderDetail(key,id).collect{
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    if (list == null) {
                        send(Resource.Error("No order Found"))
                    } else {
                        send(Resource.Success(list))
                    }
                } else {
                    send(
                        Resource.Error(
                            it.exceptionOrNull()?.localizedMessage ?: "Something went wrong "
                        )
                    )
                }
            }
        }
    }
}