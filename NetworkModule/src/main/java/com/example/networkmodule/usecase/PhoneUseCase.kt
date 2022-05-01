package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.model.ProductsItem
import com.example.networkmodule.model.Response
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.PhoneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PhoneUseCase @Inject constructor(
    private val repository: PhoneRepository
) {
    operator fun invoke():Flow<Resource<Response>> = flow {
        try {
            emit(Resource.Loading())
            val result=repository.getPhoneData()
            Log.d("Check val",""+result)
            emit(Resource.Success(result))
        }
        catch (e:HttpException){
            emit(Resource.Error(e.localizedMessage?:"Something went Wrong"))

        }catch (e:IOException){
            emit(Resource.Error("Couldn't connect to server"))
        }
    }
}