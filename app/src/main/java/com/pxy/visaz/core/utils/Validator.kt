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
        return name.length >= 3
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
            name.isEmpty() -> "Please enter input"
            name.length < 3 -> "Input must be at least 3 characters long"
            else -> null
        }
    }

    // Method to return error message if validation fails for password
    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Please enter password"
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
            email.isEmpty() -> "Please enter email"
            !isValidEmail(email) -> "Invalid email format"
            else -> null
        }
    }

    // Method to return error message if phone number validation fails
    fun getPhoneNumberError(phoneNumber: String): String? {
        return when {
            phoneNumber.isEmpty() -> "Please enter phone number"
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
            otp.isEmpty() -> "Please enter OTP"
            otp.length < 6 -> "OTP must be 6 characters long"
            otp.contains(" ") -> "OTP should not contain spaces"
            else -> null
        }
    }

    fun isInputValid(input: String?): Boolean {
        return input.isNullOrEmpty().not()
    }

    fun getInputError(input: String?): String? {
        return when {
            input.isNullOrEmpty() -> "Please enter input"
            else -> null
        }
    }

    fun isValidPAN(pan: String): Boolean {
        val panRegex = Regex("^[A-Z]{5}[0-9]{4}[A-Z]$")
        return panRegex.matches(pan)
    }

    fun getPanError(pan: String): String? {
        return when {
            pan.isEmpty() -> "Please enter pan number"
            !isValidPAN(pan) -> "Invalid pan number"
            else -> null
        }
    }

    fun isValidIndianPassport(passport: String): Boolean {
        val passportRegex = Regex("^[A-Z][0-9]{7}$")
        return passportRegex.matches(passport)
    }

    fun getPassportError(passport: String): String? {
        return when {
            passport.isEmpty() -> "Please enter passport number"
            !isValidIndianPassport(passport) -> "Invalid passport number"
            else -> null
        }
    }

    fun isValidAadhaarNumber(aadhaar: String): Boolean {
        val aadhaarRegex = Regex("^[2-9][0-9]{11}$")
        return aadhaarRegex.matches(aadhaar)
    }

    fun getAadhaarError(passport: String): String? {
        return when {
            passport.isEmpty() -> "Please enter aadhaar number"
            !isValidAadhaarNumber(passport) -> "Invalid aadhaar number"
            else -> null
        }
    }
}