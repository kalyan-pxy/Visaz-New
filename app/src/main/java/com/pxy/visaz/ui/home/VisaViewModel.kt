package com.pxy.visaz.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pxy.visaz.core.BaseViewModel
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.model.visa.VisaApplicationSubmitModel
import com.pxy.visaz.core.model.visa.VisaSubmitApplicationModel
import com.pxy.visaz.core.model.visa.VisaType
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.domain.interactors.VisaUseCase
import kotlinx.coroutines.launch

class VisaViewModel(private val useCase: VisaUseCase) : BaseViewModel() {

    val logoutObserver: MutableLiveData<Boolean> = MutableLiveData()
    val visasObserver: MutableLiveData<List<VisaApplicationModel>?> = MutableLiveData()
    val visasObserverFromResponse: MutableLiveData<List<VisaApplicationModel>?> = MutableLiveData()

    val selectedVisaTypeObserver: MutableLiveData<VisaType?> = MutableLiveData()
    val submitVisaApplicationObserver: MutableLiveData<VisaApplicationSubmitModel?> =
        MutableLiveData()

    private val _totalTravelers = MutableLiveData(1)
    val totalTravelers: LiveData<Int> = _totalTravelers

    fun logout() {
        AppPreferenceHelper.logout()
        logoutObserver.value = true
    }

    init {
        fetchVisas()
    }

    fun fetchVisas() {
        _loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.loadVisas()
            _loaderObserver.value = false
            if (result.isSuccessful) {
                visasObserver.value = result.model
                visasObserverFromResponse.value = result.model
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

    fun getTravellerCount(): Int {
        return _totalTravelers.value ?: 1
    }

    fun submitVisaApplication(
        countryId: String,
        visaType: String,
        updatedList: ArrayList<VisaApplicationDetails>
    ) {
        _loaderObserver.value = true
        viewModelScope.launch {
            val result = useCase.submitVisaApplication(
                countryId,
                visaType,
                visaApplicationDetails = updatedList
            )
            _loaderObserver.value = false
            if (result.isSuccessful) {
                submitVisaApplicationObserver.value = result.model
            } else {
                _errorObserver.value = result.errorModel
            }
        }
    }

    fun selectVisaType(vasaType: VisaType) {
        selectedVisaTypeObserver.value = vasaType
    }

    fun submitVisaApplication(visaSubmitApplicationModel: VisaSubmitApplicationModel) {
        Log.d("VisaViewModel", "===>>> submitVisaApplication: $visaSubmitApplicationModel")
    }

    fun searchVisas(toString: String) {
        visasObserverFromResponse.value?.let {
            visasObserver.value = it.filter { visa ->
                visa.name.contains(toString, true)
            }
        }
    }

}