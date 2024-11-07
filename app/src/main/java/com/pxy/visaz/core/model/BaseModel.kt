package com.pxy.visaz.core.model

data class BaseModel<T>(
    val isSuccessful: Boolean,
    val model: T? = null,
    val errorModel: ErrorModel? = null
)