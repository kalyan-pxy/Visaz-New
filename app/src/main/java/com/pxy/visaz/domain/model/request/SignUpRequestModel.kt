package com.pxy.visaz.domain.model.request

data class SignUpRequestModel(
    val name: String,
    val email: String,
    val phone: String,
    val country_code: String? = "+91",//for now its static
)