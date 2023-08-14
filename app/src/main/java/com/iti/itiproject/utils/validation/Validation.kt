package com.iti.itiproject.utils.validation

import android.util.Patterns
import com.iti.itiproject.utils.validation.*

fun validateEmail(email: String): ValidationResult<EmailError> {
    if (email.isBlank()) {
        return ValidationResult.Error(EmailError.IS_EMPTY)
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return ValidationResult.Error(EmailError.INVALID)

    }
    return ValidationResult.Success(true)

}

fun validatePassword(password: String): ValidationResult<PasswordError> {
    if (password.isBlank()) {
        return ValidationResult.Error(PasswordError.IS_EMPTY)
    }
    if (password.length < 8) {
        return ValidationResult.Error(PasswordError.IS_SHORT)
    }
    if (password.length > 20) {
        return ValidationResult.Error(PasswordError.IS_LONG)
    }
    return ValidationResult.Success(true)
}

fun validateLoginPassword(password: String): ValidationResult<LoginPasswordError> {
    if (password.isBlank()) {
        return ValidationResult.Error(LoginPasswordError.IS_EMPTY)
    }
    return ValidationResult.Success(true)
}

fun validateFullName(fullName: String): ValidationResult<FullNameError> {
    if (fullName.isBlank()) {
        return ValidationResult.Error(FullNameError.IS_EMPTY)
    }
    if (fullName.getWordsCount() < 2) {
        return ValidationResult.Error(FullNameError.IS_SHORT)
    }
    if (fullName.getWordsCount() > 4) {
        return ValidationResult.Error(FullNameError.IS_LONG)
    }
    if (fullName.any { it.isDigit() }) {
        return ValidationResult.Error(FullNameError.INVALID)
    }
    return ValidationResult.Success(true)
}

private fun String.getWordsCount(): Int {
    val words = this.trim()
    return words.split("\\s+".toRegex()).size
}

fun validatePhoneNumber(phoneNumber: String): ValidationResult<PhoneError> {
    if (phoneNumber.isBlank()) {
        return ValidationResult.Error(PhoneError.IS_EMPTY)
    }
    if (phoneNumber.length != 11) {
        return ValidationResult.Error(PhoneError.INVALID)
    }
    when (phoneNumber.subSequence(startIndex = 0, endIndex = 3).toString()) {
        "010", "011", "012", "015" -> {}
        else -> {
            return ValidationResult.Error(PhoneError.INVALID)
        }
    }

    if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
        return ValidationResult.Error(PhoneError.INVALID)
    }
    return ValidationResult.Success(true)
}

fun validateConfirmPassword(
    confirmPassword: String,
    password: String
): ValidationResult<ConfirmPasswordError> {
    if (confirmPassword.isBlank()) {
        return ValidationResult.Error(ConfirmPasswordError.IS_EMPTY)
    }
    if (confirmPassword != password) {
        return ValidationResult.Error(ConfirmPasswordError.NOT_MATCHING_PASSWORD)
    }
    return ValidationResult.Success(true)
}