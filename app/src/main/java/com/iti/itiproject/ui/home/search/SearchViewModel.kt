package com.iti.itiproject.ui.home.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.itiproject.model.api.today.TodayResponseItem
import com.iti.itiproject.ui.home.BaseRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(
    private val searchRepository: BaseRepository
) : ViewModel() {

    private val _countryData: MutableLiveData<TodayResponseItem> = MutableLiveData()
    val countryData: MutableLiveData<TodayResponseItem> = _countryData

    fun getCountryData(country: String) = viewModelScope.launch {
        safeCountryCall(country)
    }

    private fun safeCountryCall(country: String) {
        val call = searchRepository.getRetrofitSearchCountry(country)
        call.enqueue(object : Callback<TodayResponseItem> {
            override fun onResponse(
                call: Call<TodayResponseItem>,
                response: Response<TodayResponseItem>
            ) {
                _countryData.postValue(response.body())
            }

            override fun onFailure(call: Call<TodayResponseItem>, t: Throwable) {
                Log.d("suz", "onFailure: ${t.message}")
            }
        })
    }

}