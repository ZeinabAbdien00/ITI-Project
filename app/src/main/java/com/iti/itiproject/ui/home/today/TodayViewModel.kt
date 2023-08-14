package com.iti.itiproject.ui.home.today

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.itiproject.model.api.today.TodayResponse
import com.iti.itiproject.model.api.today.all.TodayAll
import com.iti.itiproject.ui.home.BaseRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayViewModel(
    private val todayRepository: BaseRepository
) : ViewModel() {

    private val _countriesData: MutableLiveData<TodayResponse> = MutableLiveData()
    val countriesData: MutableLiveData<TodayResponse> = _countriesData

    private val _allData: MutableLiveData<TodayAll> = MutableLiveData()
    val allData: MutableLiveData<TodayAll> = _allData

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCountries() = viewModelScope.launch {
        safeCountriesCall()
    }

    fun getAll() = viewModelScope.launch {
        safeAllCountriesCall()
    }

    private fun safeCountriesCall() {

        val call = todayRepository.getRetrofitCountries()
        call.enqueue(object : Callback<TodayResponse> {
            override fun onResponse(
                call: Call<TodayResponse>,
                response: Response<TodayResponse>
            ) {
                _countriesData.postValue(response.body())
                _isLoading.value = false
            }

            override fun onFailure(call: Call<TodayResponse>, t: Throwable) {
                Log.d("suz", "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    private fun safeAllCountriesCall() {

        val call = todayRepository.getAllDataToday()
        call.enqueue(object : Callback<TodayAll> {
            override fun onResponse(
                call: Call<TodayAll>,
                response: Response<TodayAll>
            ) {
                _allData.postValue(response.body())
            }

            override fun onFailure(call: Call<TodayAll>, t: Throwable) {
                Log.d("suzan", "onFailure: ${t.message}")
            }
        })
    }

}