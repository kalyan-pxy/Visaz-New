package com.pxy.visaz.domain.interactors

import com.pxy.visaz.core.model.BaseModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.domain.IRepository

class VisaUseCase(private val remoteRepository: IRepository) {
    suspend fun loadVisas(
    ): BaseModel<List<VisaApplicationModel>> {
        return remoteRepository.loadVisas()
    }
}