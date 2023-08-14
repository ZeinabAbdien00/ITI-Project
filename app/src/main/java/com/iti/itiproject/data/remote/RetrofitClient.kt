package com.iti.itiproject.data.remote

import com.iti.itiproject.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val httpLogging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun instance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(httpLogging)// Add your custom interceptor here
                    .build()
            )
            .build()

    fun apiServiceInstance(): CoronaApi = instance().create(CoronaApi::class.java)

}