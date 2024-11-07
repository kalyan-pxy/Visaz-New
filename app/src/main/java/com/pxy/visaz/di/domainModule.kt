package com.pxy.visaz.di

import com.pxy.visaz.domain.interactors.AuthUseCase
import com.pxy.visaz.domain.interactors.VisaUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { AuthUseCase(get()) }
    factory { VisaUseCase(get()) }
}
