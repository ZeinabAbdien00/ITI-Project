package com.iti.itiproject.ui.home.setting

import androidx.lifecycle.ViewModel
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.ui.home.BaseRepository

class SettingViewModel(
    private val settingRepository: BaseRepository,
    val dataStore: DataStoreImplementation
) : ViewModel() {

    suspend fun logOut(dataStore: DataStoreImplementation) {
        settingRepository.logOut(dataStore)
    }
}