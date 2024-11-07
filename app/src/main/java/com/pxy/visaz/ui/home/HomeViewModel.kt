package com.pxy.visaz.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.domain.interactors.AuthUseCase
import com.pxy.visaz.domain.model.Inspections

class HomeViewModel(private val useCase: AuthUseCase) : ViewModel() {

    val logoutObserver: MutableLiveData<Boolean> = MutableLiveData()

    fun logout() {
        AppPreferenceHelper.logout()
        logoutObserver.value = true
    }


}