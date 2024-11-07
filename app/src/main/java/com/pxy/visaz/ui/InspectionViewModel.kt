package com.pxy.visaz.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pxy.visaz.domain.interactors.AuthUseCase
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.Inspections
import kotlinx.coroutines.launch

class InspectionViewModel(private val useCase: AuthUseCase) : ViewModel() {

    val inspectionsObserver: MutableLiveData<Inspections> = MutableLiveData()
    val inspectionDetailsObserver: MutableLiveData<InspectionDetailsUpdateModel> = MutableLiveData()

    fun getInspections() {
        viewModelScope.launch {
            val list = useCase.loadInspections()
            inspectionsObserver.postValue(list)
        }
    }

    fun refreshInspections() {
        viewModelScope.launch {
            val list = useCase.refreshInspections()
            inspectionsObserver.postValue(list)
        }
    }

    fun updateInspection(
        groupPosition: Int,
        childPosition: Int,
        inspectionDetails: InspectionDetails
    ) {
        viewModelScope.launch {
            val inspectionDetails = useCase.updateInspection(inspectionDetails)
            inspectionDetailsObserver.postValue(
                InspectionDetailsUpdateModel(
                    groupPosition,
                    childPosition,
                    inspectionDetails
                )
            )
        }
    }
}