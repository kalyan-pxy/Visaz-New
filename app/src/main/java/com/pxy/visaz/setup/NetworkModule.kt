package com.pxy.visaz.setup

import android.content.Context
import android.content.Intent
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.ui.authentication.PreAuthActivity
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

object NetworkModule {

    fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
}