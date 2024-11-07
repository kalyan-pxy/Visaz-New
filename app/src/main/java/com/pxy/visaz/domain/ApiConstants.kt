package com.pxy.visaz.domain

object ApiConstants {
    //Base url comes from build config
    //const val BASE_URL = "https://visa-z.com/Python312"
    private const val API = "Python312"

    const val LOGIN_ENDPOINT = "$API/login"
    const val SIGN_UP_ENDPOINT = "$API/signup"
    const val CREATE_PASSWORD_ENDPOINT = "$API/create-password"
}