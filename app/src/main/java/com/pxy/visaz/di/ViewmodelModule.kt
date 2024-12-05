package com.pxy.visaz.di

import com.pxy.visaz.ui.authentication.LoginViewModel
import com.pxy.visaz.ui.home.HomeViewModel
import com.pxy.visaz.ui.home.VisaViewModel
import com.pxy.visaz.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { VisaViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}