package com.pxy.visaz.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pxy.visaz.core.model.ErrorModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.domain.interactors.AuthUseCase
import com.pxy.visaz.domain.interactors.VisaUseCase
import com.pxy.visaz.domain.model.Inspections
import kotlinx.coroutines.launch

class VisaViewModel(private val useCase: VisaUseCase) : ViewModel() {

    val logoutObserver: MutableLiveData<Boolean> = MutableLiveData()
    val loaderObserver: MutableLiveData<Boolean> = MutableLiveData()
    val visasObserver: MutableLiveData<List<VisaApplicationModel>?> = MutableLiveData()
    val errorObserver: MutableLiveData<ErrorModel?> = MutableLiveData()

    fun logout() {
        AppPreferenceHelper.logout()
        logoutObserver.value = true
    }

    fun fetchVisas(){
        loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.loadVisas()
            loaderObserver.value = false
            if (result.isSuccessful) {
                visasObserver.value = result.model
            } else {
                visasObserver.value = result.model
                errorObserver.value = result.errorModel
            }

        }
    }

}