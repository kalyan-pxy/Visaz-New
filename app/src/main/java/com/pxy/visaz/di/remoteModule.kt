package com.pxy.visaz.di

import com.pxy.visaz.BuildConfig
import com.pxy.visaz.setup.AppServiceFactory
import com.pxy.visaz.setup.HttpClientFactory
import com.pxy.visaz.setup.ServiceFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteModule = module {
    single(named("HTTP_CLIENT")) {
        HttpClientFactory(
            BuildConfig.DEBUG,
            get()
        )
    }
    single(named("SERVICE_FACTORY")) { ServiceFactory(BuildConfig.SERVER_URL) }
    single(named("APP_SERVICE")) {
        AppServiceFactory(get(named("HTTP_CLIENT"))).getAppService(get(named("SERVICE_FACTORY")))
    }
}