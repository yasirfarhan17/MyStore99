package com.example.networkmodule.usecase

import android.util.Log
import com.example.networkmodule.model.User
import com.example.networkmodule.network.AuthResource
import com.example.networkmodule.network.Resource
import com.example.networkmodule.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(phoneNumber: String, password: String): Flow<AuthResource> = flow {
        emit(AuthResource.Loading)
        repository.login(phoneNumber, password).collect {
            when (it) {
                is Resource.Error -> emit(AuthResource.Error(it.message.toString()))
                is Resource.Loading -> emit(AuthResource.Loading)
                is Resource.Success -> {
                    if (it.data == null || it.data.exists()
                            .not() || it.data.key.isNullOrEmpty() || it.data.value == null
                    ) {
                        emit(AuthResource.NoUserFound)
                        return@collect
                    }
                    val obj = it.data.getValue(User::class.java)
                    //Log.d("checkDoLogin", "insidepass${obj?.password} $password")
                    if (obj?.password.equals(password)) {

                        if (obj?.otpVerified!!) {

                            //emit(AuthResource.OtpRequired)
                            emit(AuthResource.Success)
                            return@collect
                        } else {
                            emit(AuthResource.OtpRequired)
                            return@collect
                        }
                    } else emit(AuthResource.WrongPassword)
                }
            }
        }


    }
}