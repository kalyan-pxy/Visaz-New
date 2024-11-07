package com.pxy.visaz

import android.app.Application
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.di.domainModule
import com.pxy.visaz.di.remoteModule
import com.pxy.visaz.di.viewModelModule
import localModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        init()
        // Added Secure shared preferences
        AppPreferenceHelper.createSharedPreferences(applicationContext)
    }

    private fun init() {
        startKoin {
            androidContext(this@MyApplication)
            modules(
                viewModelModule,
                localModule,
                remoteModule,
                domainModule
            )
        }
    }

}