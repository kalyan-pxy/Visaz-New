package com.pxy.visaz.core.extension

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.initBackNavigationHandler(function: () -> Unit) {
    onBackPressedDispatcher
        .addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                function.invoke()
            }
        })
}