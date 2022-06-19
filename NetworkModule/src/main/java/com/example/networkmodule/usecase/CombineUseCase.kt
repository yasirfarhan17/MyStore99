package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.model.ProductModel
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.FirebaseDatabaseRepository
import com.example.networkmodule.util.io
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class CombineUseCase
@Inject constructor(
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository,
) {
    operator fun invoke(): Flow<Resource<Pair<List<ProductModel>, List<CartEntity>>>> =
        flow {
            emit(Resource.Loading())
            io {

                var product = async { firebaseDatabaseRepository.getAllProduct() }
                var cart = async { firebaseDatabaseRepository.getCart() }
                val newList = ArrayList<ProductModel>()
                var result = combine(product.await(), cart.await()) { product, cart ->
                    Log.d("insideCombine", product.toString() + " " + cart.toString())
                    cart.getOrNull()?.forEach { cartItem ->
                        product.getOrNull()?.forEach { productItem ->
                            if (cartItem.products_name.equals(productItem.products_name)) {
                                newList.add(cartItem.toProductNewModel())
                            } else {
                                newList.add(productItem)
                            }

                        }
                    }
                    return@combine Pair(newList.toList(), cart.getOrNull()?.toList() ?: emptyList<CartEntity>())
                }
                result.collect{
                    try {
//                    emit(Resource.Success(Pair((product.await(), cart.await()))))
                        emit(Resource.Success(it))
                    }
                    catch (e:Exception){
                        emit(Resource.Error(e.message.toString()))
                    }
                }




            }
        }
}