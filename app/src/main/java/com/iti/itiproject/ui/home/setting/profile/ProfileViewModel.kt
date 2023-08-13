package com.iti.itiproject.ui.home.setting.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.model.auth.AuthModel
import kotlinx.coroutines.launch

class ProfileViewModel(val dataStore: DataStoreImplementation) : ViewModel() {

    private val _userData: MutableLiveData<AuthModel> = MutableLiveData()
    val userData: MutableLiveData<AuthModel> = _userData

    fun getUserData() = callUserData()

    private fun callUserData() {
        viewModelScope.launch {
            _userData.value = dataStore.getUser()
        }
    }

    suspend fun saveUserDataAndLogFlag(user: AuthModel) {
        // save user data to data store
        dataStore.setLogged(true)
        dataStore.setUSerLogged(true)
        dataStore.saveUser(user)
    }
}