package com.iti.itiproject.utils.validation


sealed class ValidationResult<T> {
    data class Success<T>(val success: Boolean = true) : ValidationResult<T>()
    data class Error<T>(val type: T) : ValidationResult<T>()
}

enum class EmailError {
    INVALID, IS_EMPTY
}

enum class PasswordError {
    IS_EMPTY, IS_SHORT, IS_LONG
}

enum class LoginPasswordError {
    IS_EMPTY
}

enum class FullNameError {
    IS_EMPTY, IS_SHORT, IS_LONG, INVALID
}

enum class PhoneError {
    IS_EMPTY, INVALID
}

enum class ConfirmPasswordError {
    IS_EMPTY, NOT_MATCHING_PASSWORD
}