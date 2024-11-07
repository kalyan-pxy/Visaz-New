package com.pxy.visaz.setup

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object NetworkModule {

    fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    /*fun basicHeaderInterceptor() = Interceptor {
        val request = it.request().newBuilder()
            .addHeader(
                AppConstant.AUTHORIZATION,
                "Bearer ${BluboyPreferenceHelper.getAuthKey()}"
            )
            .addHeader(AppConstant.APPVERSION, BuildConfig.VERSION_NAME)
            .addHeader(AppConstant.X_API_KEY, BuildConfig.X_API_KEY)
            .addHeader(
                AppConstant.NOTIFICATION_PERMISSION_STATUS,
                BluboyPreferenceHelper.getNotificationPermissionStatus().toString()
            )
            .build()
        it.proceed(request)
    }*/

    fun createHeadersAriaInterceptor() = Interceptor {
        val request = it.request().newBuilder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
        it.proceed(request)
    }
}