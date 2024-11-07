package com.pxy.visaz.domain.model.request

data class CreatePasswordRequestModel(
    val email: String,
    val otp: String,
    val password: String,
    val confirm_password: String
)