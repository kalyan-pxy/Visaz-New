package com.pxy.visaz.setup

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HttpClientFactory(
    debug: Boolean,
) {
    val abstractClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
        if (debug)
            builder.addNetworkInterceptor(NetworkModule.createHttpLoggingInterceptor())
        //builder.addInterceptor(NetworkConnectionInterceptor(androidContext))
        builder.build()
    }
}