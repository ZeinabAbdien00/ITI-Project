package com.iti.itiproject.data.datastore

import com.google.firebase.auth.FirebaseUser
import com.iti.itiproject.model.auth.AuthModel

interface DataStore {

    suspend fun getUser(): AuthModel

    suspend fun isLoggedIn(): Boolean

    suspend fun clearAllData()

    suspend fun setUSerLogged(isLogged: Boolean)

    suspend fun getUserName(): String

    suspend fun getUserEmail(): String

    suspend fun setLogged(isLogged: Boolean)

    suspend fun saveUser(saveData: AuthModel)

    suspend fun isPassedOnBoarding(): Boolean

    suspend fun setPassedOnBoarding(isPassed: Boolean)

    suspend fun setDarkMode(isDark: Boolean)

    suspend fun getDarkMode(): Boolean

    suspend fun getUserImageUri(): String

    suspend fun getUserId(): String

//    suspend fun getCurrentUser(): FirebaseUser
//
//    suspend fun setCurrentUser(user: FirebaseUser)


//    suspend fun setLanguage(language: String)
//
//    suspend fun getLanguage(): String


}