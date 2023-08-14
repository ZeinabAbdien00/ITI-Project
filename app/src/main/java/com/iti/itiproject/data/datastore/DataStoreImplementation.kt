package com.iti.itiproject.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iti.itiproject.model.auth.AuthModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore("user_data")

class DataStoreImplementation(
    appContext: Context?,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DataStore {

    private val dataStoreO by lazy {
        appContext?.dataStore
    }

    companion object {
        const val USER_NAME = "userName"
        const val USER_PHONE = "userPhone"
        const val USER_EMAIL = "userEmail"
        const val USER_ID = "userId"
        const val USER_IMAGE_URL = "userImage"
        const val IS_LOGGED_IN = "loggedIn"
        const val IS_PASSED_ON_BOARDING = "passedIntro"
        const val LANGUAGE = "language"
        const val DARK_MODE = "isDark"
        const val USER_PASSWORD = "savePassword"
        const val FIREBASE_USER = "savePassword"

    }

    override suspend fun getUser(): AuthModel {
        var name: String? = null
        var email: String? = null
        var phone: String? = null
        var password: String? = null
        var imageUrl: String? = null
        var userId: String? = null

        dataStoreO!!.data.map { settings ->
            name = settings[stringPreferencesKey(USER_NAME)]
            email = settings[stringPreferencesKey(USER_EMAIL)]
            phone = settings[stringPreferencesKey(USER_PHONE)]
            password = settings[stringPreferencesKey(USER_PASSWORD)]
            imageUrl = settings[stringPreferencesKey(USER_IMAGE_URL)]
            userId = settings[stringPreferencesKey(USER_ID)]

        }.first().toString()

        return AuthModel(
            userId = userId,
            fullName = name,
            email = email,
            phone = phone,
            password = password,
            imgUri = imageUrl
        )
    }

    override suspend fun isLoggedIn(): Boolean {
        return withContext(dispatcher) {
            dataStoreO?.data?.map { settings ->
                settings[booleanPreferencesKey(IS_LOGGED_IN)]
            }?.first() ?: false
        }
    }

    override suspend fun clearAllData() {
        dataStoreO?.edit { it.clear() }
        setPassedOnBoarding(true)
    }

    override suspend fun setUSerLogged(isLogged: Boolean) {
        withContext(dispatcher) {
            dataStoreO!!.edit { settings ->
                settings[booleanPreferencesKey(IS_LOGGED_IN)] = isLogged
            }
        }
    }

    override suspend fun getUserName(): String {
        return dataStoreO!!.data.map { settings ->
            settings[stringPreferencesKey(USER_NAME)]
        }.first().toString()
    }

    override suspend fun getUserEmail(): String {
        return dataStoreO!!.data.map { settings ->
            settings[stringPreferencesKey(USER_EMAIL)]
        }.first().toString()
    }

    override suspend fun setLogged(isLogged: Boolean) {
        withContext(dispatcher) {
            dataStoreO?.edit { settings ->
                settings[booleanPreferencesKey(IS_LOGGED_IN)] = isLogged
            }
        }
    }

    override suspend fun saveUser(saveData: AuthModel) {
        val name = saveData.fullName ?: "No Name"
        val email = saveData.email ?: "No email"
        val phone = saveData.phone ?: "No phone"
        val password = saveData.password ?: "No password"
        val imageUri = saveData.imgUri ?: ""
        val userId = saveData.userId ?: ""

        dataStoreO?.edit { settings ->
            settings[stringPreferencesKey(USER_NAME)] = name
            settings[stringPreferencesKey(USER_EMAIL)] = email
            settings[stringPreferencesKey(USER_PHONE)] = phone
            settings[stringPreferencesKey(USER_PASSWORD)] = password
            settings[stringPreferencesKey(USER_IMAGE_URL)] = imageUri
            settings[stringPreferencesKey(USER_ID)] = userId
        }
    }

    override suspend fun isPassedOnBoarding(): Boolean = withContext(dispatcher) {
        dataStoreO!!.data.map { settings ->
            settings[booleanPreferencesKey(IS_PASSED_ON_BOARDING)] ?: false
        }.first()
    }

    override suspend fun setPassedOnBoarding(isPassed: Boolean) {
        withContext(dispatcher) {
            dataStoreO!!.edit { settings ->
                settings[booleanPreferencesKey(IS_PASSED_ON_BOARDING)] = isPassed
            }
        }
    }

    override suspend fun setDarkMode(isDark: Boolean) {
        dataStoreO!!.edit { settings ->
            settings[booleanPreferencesKey(DARK_MODE)] = isDark
        }
    }

    override suspend fun getDarkMode(): Boolean = withContext(dispatcher) {
        dataStoreO!!.data.map { settings ->
            settings[booleanPreferencesKey(DARK_MODE)] ?: true
        }.first()
    }

    override suspend fun getUserImageUri(): String = withContext(dispatcher) {
        dataStoreO!!.data.map { settings ->
            settings[stringPreferencesKey(USER_IMAGE_URL)]
        }.first().toString()
    }

    override suspend fun getUserId(): String = withContext(dispatcher) {
        dataStoreO!!.data.map { settings ->
            settings[stringPreferencesKey(USER_ID)]
        }.first().toString()
    }
}