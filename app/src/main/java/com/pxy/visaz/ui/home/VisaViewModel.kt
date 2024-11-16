package com.pxy.visaz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pxy.visaz.core.BaseViewModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.domain.interactors.VisaUseCase
import kotlinx.coroutines.launch

class VisaViewModel(private val useCase: VisaUseCase) : BaseViewModel() {

    val logoutObserver: MutableLiveData<Boolean> = MutableLiveData()
    val visasObserver: MutableLiveData<List<VisaApplicationModel>?> = MutableLiveData()

    private val _totalTravelers = MutableLiveData(1)
    val totalTravelers: LiveData<Int> = _totalTravelers

    fun logout() {
        AppPreferenceHelper.logout()
        logoutObserver.value = true
    }

    fun fetchVisas() {
        _loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.loadVisas()
            _loaderObserver.value = false
            if (result.isSuccessful) {
                visasObserver.value = result.model
            } else {
                _errorObserver.value = result.errorModel
            }

        }
    }

    fun addTraveller() {
        _totalTravelers.value = _totalTravelers.value?.plus(1)
    }

    fun removeTraveller() {
        if (_totalTravelers.value == 1) return
        _totalTravelers.value = _totalTravelers.value?.minus(1)
    }

}