package com.iti.itiproject.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.model.auth.AuthModel
import kotlinx.coroutines.Dispatchers

class HomeViewModel() : ViewModel() {

    suspend fun saveUserDataAndLogFlag(user: AuthModel, context: Context? , dataStore: DataStoreImplementation) {
        // save user data to data store
        dataStore.setLogged(true)
        dataStore.setUSerLogged(true)
        dataStore.saveUser(user)
    }
}