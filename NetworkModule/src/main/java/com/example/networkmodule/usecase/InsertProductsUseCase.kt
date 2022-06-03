package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.database.entity.ProductEntity
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class InsertProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(list: ArrayList<ProductEntity>) = flow{
        try {
            emit(Resource.Loading())
            val result=repository.insertItems(list)
            Log.d("usecase",""+result)
            emit(Resource.Success(result))
        }
        catch (e:Exception){
            emit(Resource.Error(e.localizedMessage?:"Something went wrong"))
        }
        catch (E:IOException)
        {
            emit(Resource.Error("Couldn't connect to db"))
        }

    }
}