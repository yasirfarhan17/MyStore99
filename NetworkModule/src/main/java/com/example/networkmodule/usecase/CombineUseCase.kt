package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CombineUseCase
@Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
) {
    operator fun invoke(): Flow<Resource<Pair<List<ProductModel>, List<CartEntity>>>> =
        flow {
            emit(Resource.Loading())

            var product =
                withContext(Dispatchers.Default) { firebaseDatabaseRepository.getAllProduct() }
            var cart =
                withContext(Dispatchers.Default) { firebaseDatabaseRepository.getCart() }
            val newList = ArrayList<ProductModel>()
            var result = combine(product, cart) { product, cart ->
                Log.d("insideCombine", product.toString() + " " + cart.toString())

                newList.clear()
                product.getOrNull()?.forEach { productItem ->

                    if(cart.getOrNull().isNullOrEmpty()){
                        newList.clear()
                        newList.addAll(product.getOrNull()!!)
                        return@combine Pair(
                            newList.toList(),
                            ArrayList<CartEntity>()
                        )
                    }
                    cart.getOrNull()?.forEach { cartItem ->
                        if (cartItem.products_name.equals(productItem.products_name) && !newList.contains(productItem)) {
                            newList.add(cartItem.toProductNewModel())

                        } else {
                            newList.add(productItem)
                        }
                    }
                }
                var newVal=ArrayList<ProductModel>()
                newVal.addAll(newList.distinct())
                return@combine Pair(
                    newVal.toList(),
                    cart.getOrNull() ?: ArrayList<CartEntity>()
                )
            }

            result.collect {
                emit(Resource.Success(it))

            }


        }
}