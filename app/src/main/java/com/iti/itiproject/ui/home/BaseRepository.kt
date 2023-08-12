package com.iti.itiproject.ui.home

import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.data.remote.RetrofitClient

open class BaseRepository {

    companion object {
        private val retrofitObject = RetrofitClient.apiServiceInstance()
    }

    fun getRetrofitCountries() =
        getCountries()

    fun getAllDataToday() =
        getAllTodayData()

    fun getRetrofitSearchCountry(country: String) =
        getSearchQuotes(country)

    private fun getCountries() =
        retrofitObject.getCountriesData()

    private fun getAllTodayData() =
        retrofitObject.getGlobalData()


    private fun getSearchQuotes(country: String) =
        retrofitObject.getCountryData(country)

    suspend fun logOut(dataStore : DataStoreImplementation) {
        dataStore.setUSerLogged(false)
        dataStore.clearAllData()
    }

}