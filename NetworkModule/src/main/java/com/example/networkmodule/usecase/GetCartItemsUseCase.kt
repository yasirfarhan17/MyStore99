package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.database.entity.CartEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
)
{
    operator fun invoke():Flow<Resource<List<CartEntity>>> = flow {
        try{
            emit(Resource.Loading())
            val result=cartRepository.getCartItem()
            Log.d("cartcheck",""+result)
            emit(Resource.Success(result))
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage?:"Something went wrong"))
        }
        catch (E: IOException)
        {
            emit(Resource.Error("Couldn't connect to db"))
        }
    }
}