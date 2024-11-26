package com.pxy.visaz.domain.interactors

import com.pxy.visaz.core.model.BaseModel
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.model.visa.VisaApplicationSubmitModel
import com.pxy.visaz.domain.IRepository
import com.pxy.visaz.domain.model.request.SubmitVisaApplicationRequestModel

class VisaUseCase(private val remoteRepository: IRepository) {
    suspend fun loadVisas(
    ): BaseModel<List<VisaApplicationModel>> {
        return remoteRepository.loadVisas()
    }

    suspend fun submitVisaApplication(
        country: String,
        visaType: String,
        visaApplicationDetails: ArrayList<VisaApplicationDetails>
    ): BaseModel<VisaApplicationSubmitModel> {
        return remoteRepository.submitVisaApplication(
            SubmitVisaApplicationRequestModel(
                country = country,
                visaType = visaType,
                visaApplicationDetails = visaApplicationDetails
            )
        )
    }
}