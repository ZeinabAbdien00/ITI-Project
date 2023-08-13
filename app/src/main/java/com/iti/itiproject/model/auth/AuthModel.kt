package com.iti.itiproject.model.auth

import androidx.annotation.Keep

@Keep
data class AuthModel(
    val userId: String? = "",
    val fullName: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val password: String? = "",
    val imgUri: String? = ""
)