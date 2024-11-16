package com.pxy.visaz.setup

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class HttpClientFactory(
    debug: Boolean,
    context: Context
) {
    val abstractClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
        if (debug)
            builder.addNetworkInterceptor(NetworkModule.createHttpLoggingInterceptor())
        //builder.addInterceptor(NetworkConnectionInterceptor(androidContext))
        builder.build()
    }
}