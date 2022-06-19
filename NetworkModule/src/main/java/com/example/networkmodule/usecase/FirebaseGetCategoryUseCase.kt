package com.example.networkmodule.usecase

import android.content.Context
import com.example.networkmodule.model.CategoryModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import com.example.networkmodule.util.computation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class FirebaseGetCategoryUseCase @Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    operator fun invoke(): Flow<Resource<List<CategoryModel>>> = channelFlow {
        computation {
            send(Resource.Loading())
            firebaseDatabaseRepository.getCategory().collect {
                if (it.isSuccess) {
                    val list = it.getOrNull()
                    if (list == null) {
                        send(Resource.Error("No Category Found"))
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