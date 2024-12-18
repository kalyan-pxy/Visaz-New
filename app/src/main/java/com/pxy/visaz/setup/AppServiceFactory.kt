package com.pxy.visaz.setup

import com.pxy.visaz.data.remote.AppService

class AppServiceFactory(private val httpClientFactory: HttpClientFactory) {

    fun getAppService(serviceFactory: ServiceFactory): AppService {
        val httpClient = httpClientFactory.abstractClient.newBuilder()
            //.addInterceptor(NetworkModule.basicHeaderInterceptor())
            .build()
        val appService = serviceFactory.retrofitInstance.newBuilder().client(httpClient).build()
        return appService.create(AppService::class.java)
    }

}

