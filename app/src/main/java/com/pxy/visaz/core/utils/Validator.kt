package com.pxy.visaz.core.utils

class Validator {

    // Method to validate the username
    fun isValidUsername(username: String): Boolean {
        // Username must be at least 3 characters long and should not contain spaces
        return isValidEmail(username)
    }

    // Method to validate the username
    fun isValidName(name: String): Boolean {
        // Username must be at least 3 characters long and should not contain spaces
        return name.length >= 3 && !name.contains(" ")
    }

    // Method to validate the password
    fun isValidPassword(password: String): Boolean {
        // Password must be at least 8 characters long
        if (password.length < 8) return false

        // Regex pattern to check password contains at least:
        // - One uppercase letter
        // - One lowercase letter
        // - One digit
        // - One special character
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")

        // Return true if the password matches the pattern, else false
        return passwordPattern.matches(password)
    }

    // Method to return error message if validation fails for username
    fun getUsernameError(username: String): String? {
        return getEmailError(username)
    }

    // Method to return error message if validation fails for username
    fun getNameError(name: String): String? {
        return when {
            name.isEmpty() -> "Name cannot be empty"
            name.length < 3 -> "Name must be at least 3 characters long"
            name.contains(" ") -> "Name should not contain spaces"
            else -> null
        }
    }

    // Method to return error message if validation fails for password
    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters long"
            !password.contains(Regex(".*[A-Z].*")) -> "Password must contain at least one uppercase letter"
            !password.contains(Regex(".*[a-z].*")) -> "Password must contain at least one lowercase letter"
            !password.contains(Regex(".*\\d.*")) -> "Password must contain at least one number"
            !password.contains(Regex(".*[@#\$%^&+=!].*")) -> "Password must contain at least one special character"
            else -> null
        }
    }

    // Method to validate email
    fun isValidEmail(email: String): Boolean {
        // Regex pattern to validate email address
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$")
        return emailPattern.matches(email)
    }

    // Method to validate phone number (for India, 10 digits)
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Phone number should be exactly 10 digits and contain only numbers
        val phonePattern = Regex("^\\d{10}$")
        return phonePattern.matches(phoneNumber)
    }

    // Method to return error message if email validation fails
    fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> "Email cannot be empty"
            !isValidEmail(email) -> "Invalid email format"
            else -> null
        }
    }

    // Method to return error message if phone number validation fails
    fun getPhoneNumberError(phoneNumber: String): String? {
        return when {
            phoneNumber.isEmpty() -> "Phone number cannot be empty"
            !isValidPhoneNumber(phoneNumber) -> "Invalid phone number format (must be 10 digits)"
            else -> null
        }
    }

    // Method to validate the otp
    fun isValidOtp(otp: String): Boolean {
        // Username must be at least 3 characters long and should not contain spaces
        return otp.length >= 6 && !otp.contains(" ")
    }

    // Method to return error message if validation fails for username
    fun getOtpError(otp: String): String? {
        return when {
            otp.isEmpty() -> "OTP cannot be empty"
            otp.length < 6 -> "OTP must be 6 characters long"
            otp.contains(" ") -> "OTP should not contain spaces"
            else -> null
        }
    }
}