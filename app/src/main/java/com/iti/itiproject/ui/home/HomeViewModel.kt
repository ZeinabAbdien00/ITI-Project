package com.iti.itiproject.ui.home

import androidx.lifecycle.ViewModel
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.model.auth.AuthModel

class HomeViewModel() : ViewModel() {

    suspend fun saveUserDataAndLogFlag(user: AuthModel, dataStore: DataStoreImplementation) {
        dataStore.setLogged(true)
        dataStore.setUSerLogged(true)
        dataStore.saveUser(user)
    }
}