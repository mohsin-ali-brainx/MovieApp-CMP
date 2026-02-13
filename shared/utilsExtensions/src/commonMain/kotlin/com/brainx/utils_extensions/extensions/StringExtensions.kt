package com.brainx.utils_extensions.extensions

import com.brainx.utils_extensions.constants.ExtConstants


fun String.trimExtraSpaces(): String = replace("\\s+".toRegex(), " ").trim()

fun String.capFirstLetter(): String {
    if (isNullOrEmpty()) return ExtConstants.StringConstants.EMPTY
    return substring(ExtConstants.IntegerConstants.ZERO, ExtConstants.IntegerConstants.ONE)
        .uppercase() + substring(ExtConstants.IntegerConstants.ONE)
}

/**
 * Validates if the string is a properly formatted email address
 * Pattern matches standard email format (name@domain.tld)
 */
fun String.isValidEmail(): Boolean {
    return this.matches(ExtConstants.ExtRegexUtils.EMAIL_REGEX.toRegex())
}

/**
 * Validates if the string is a strong password
 * Requirements:
 * - At least 8 characters
 * - Contains at least one lowercase letter
 * - Contains at least one uppercase letter
 * - Contains at least one digit
 * - Contains at least one special character
 */
fun String.isValidPassword(): Boolean {
    return this.matches(ExtConstants.ExtRegexUtils.PASSWORD_REGEX.toRegex())
}

/**
 * Validates if the string is a valid phone number
 * Accepts formats: +1234567890, 123-456-7890, (123) 456-7890
 * Minimum 10 digits, allows country code and formatting characters
 */
fun String.isValidPhone(): Boolean {
    return this.matches(ExtConstants.ExtRegexUtils.PHONE_REGEX.toRegex())
}

/**
 * Validates if the string is a valid phone number
 * Accepts formats: +1234567890, 123-456-7890, (123) 456-7890
 * Minimum 10 digits, allows country code and formatting characters
 */
/**
 * Validates if the string is a valid phone number with customizable length constraints
 * @param minLength Minimum allowed length of phone number (default is 1)
 * @param maxLength Maximum allowed length of phone number
 * @return true if valid phone number, false otherwise
 */
fun String.isValidPhoneNumber(
    minLength: Int = ExtConstants.IntegerConstants.ONE,
    maxLength: Int
): Boolean {
    // Check if string is empty or contains non-digit characters
    if (this.isBlank() || !Regex("^\\d+\$").matches(this)) {
        return false
    }

    // Check length constraints
    if (this.length > maxLength || this.length < minLength) {
        return false
    }

    // Create regex pattern for exact length constraints
    val pattern = "^\\d{$minLength,$maxLength}\$"
    return Regex(pattern).matches(this)
}

/**
 * Simple validation to check if string contains only letters
 */
fun String.containsOnlyLetters(): Boolean {
    val pattern = "^[A-Za-z\\s]+\$"
    return this.matches(ExtConstants.ExtRegexUtils.ONLY_LETTERS.toRegex())
}

/**
 * Simple validation to check if string contains only numbers
 */
fun String.containsOnlyNumbers(): Boolean {
    val pattern = "^[0-9]+\$"
    return this.matches(ExtConstants.ExtRegexUtils.ONLY_DIGIT.toRegex())
}

/**
 * Simple validation to check if string is valid username
 */
fun String.isValidUsername(): Boolean {
    val pattern = "^[0-9]+\$"
    return this.matches(ExtConstants.ExtRegexUtils.USERNAME_REGEX.toRegex())
}