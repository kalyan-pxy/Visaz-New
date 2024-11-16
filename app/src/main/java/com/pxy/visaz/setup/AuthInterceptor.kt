package com.pxy.visaz.setup

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.pxy.visaz.R
import com.pxy.visaz.core.AppConstants
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.ui.authentication.PreAuthActivity
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(
                AppConstants.AUTHORIZATION,
                "Bearer ${AppPreferenceHelper.auth.orEmpty()}"
            )
            .addHeader(
                AppConstants.UNIQUE_HASH,
                AppPreferenceHelper.hash.orEmpty()
            ).build()

        val response = chain.proceed(request)

        if (response.code == 403) {
            handleUnauthorized()
        }

        return response
    }

    private fun handleUnauthorized() {
        // Clear any stored user data
        AppPreferenceHelper.clearTokens()
/*
        Toast.makeText(
            context,
            context.getString(R.string.error_message_unauthorized), Toast.LENGTH_SHORT
        ).show()*/

        // Redirect to login activity
        val intent = Intent(context, PreAuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}