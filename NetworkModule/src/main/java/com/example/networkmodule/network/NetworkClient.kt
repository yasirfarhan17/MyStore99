package com.example.networkmodule.network

import com.example.networkmodule.api.EndPoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val TIMEOUT_IN_SECONDS=20L

    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ):Retrofit=Retrofit.Builder()
        .baseUrl(EndPoint.Base_Url)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun provideOkHttp(
        headerInterceptor:HeaderInterceptor,
    networkManager: NetworkManager
    ):OkHttpClient{
        val httpBuilder=OkHttpClient.Builder()
        httpBuilder.apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level=HttpLoggingInterceptor.Level.BODY
            })
        }
        httpBuilder.apply {
            addInterceptor(NoInternetInterceptor(networkManager))
            addInterceptor(headerInterceptor)
            connectTimeout(TIMEOUT_IN_SECONDS,TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_IN_SECONDS,TimeUnit.SECONDS)
            readTimeout(TIMEOUT_IN_SECONDS,TimeUnit.SECONDS)
        }
        return httpBuilder.build()
    }
}