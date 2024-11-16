package com.pxy.visaz.domain.model.response

data class LoginResponseModel(
    val message: String? = null,
    val token: String? = null,
    val role: String? = null,
    val uniqueHash: String? = null,
    val status: Boolean
)
