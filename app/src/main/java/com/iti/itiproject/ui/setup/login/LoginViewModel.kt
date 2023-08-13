package com.iti.itiproject.ui.setup.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.model.auth.AuthModel
import com.iti.itiproject.utils.validation.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoginViewModel : ViewModel() {

    @Inject
    lateinit var dataStore: DataStoreImplementation

    private val _email = MutableLiveData<EmailError?>()
    val email: LiveData<EmailError?> = _email

    private val _password = MutableLiveData<LoginPasswordError?>()
    val password: LiveData<LoginPasswordError?> = _password

    private val _noAccount = MutableLiveData<String>()
    val noAccount: LiveData<String> = _noAccount

    private val _generalError = MutableLiveData<String>()
    val generalError: LiveData<String> = _generalError


    fun isValidLogin(email: String, password: String): Boolean {

        val validationEmail = validateEmail(email = email)
        val validationPassword = validateLoginPassword(password = password)
        val isValidEmail: Boolean
        val isValidPassword: Boolean

        when (validationEmail) {
            is ValidationResult.Error -> {
                _email.value = validationEmail.type
                isValidEmail = false
            }
            is ValidationResult.Success -> {
                _email.value = null
                isValidEmail = true
            }
        }

        when (validationPassword) {
            is ValidationResult.Error -> {
                _password.value = validationPassword.type
                isValidPassword = false
            }
            is ValidationResult.Success -> {
                _password.value = null
                isValidPassword = true
            }
        }
        return isValidEmail && isValidPassword
    }

    suspend fun saveUserDataAndLogFlag(user: AuthModel, context: Context?) {
        // save user data to data store
        dataStore = DataStoreImplementation(appContext = context, Dispatchers.IO)
        dataStore.setLogged(true)
        dataStore.setUSerLogged(true)
        dataStore.saveUser(user)
    }


}