package com.iti.itiproject.data.remote

import com.iti.itiproject.model.api.today.TodayResponse
import com.iti.itiproject.model.api.today.TodayResponseItem
import com.iti.itiproject.model.api.today.all.TodayAll
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoronaApi {

    @GET("countries")
    fun getCountriesData(
        @Query("yesterday") yesterday: Boolean = true,
        @Query("sort") sort: String = "true"
    ): Call<TodayResponse>

    @GET("all")
    fun getGlobalData(@Query("yesterday") yesterday: Boolean = true): Call<TodayAll>

    @GET("countries/{query}")
    fun getCountryData(
        @Path("query") query: String,
        @Query("yesterday") yesterday: Boolean = true,
        @Query("strict") strict: Boolean = true
    ): Call<TodayResponseItem>

}