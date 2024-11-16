package com.pxy.visaz.data.local

import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.local.database.AppDatabase
import com.pxy.visaz.domain.model.response.LoginResponseModel

class LocalDataSource(private val database: AppDatabase) {

    suspend fun storeInspections(inspectionDtos: List<InspectionDto>) {
        database.inspectionDao().insertAllInspections(inspectionDtos)
    }

    suspend fun retrieveInspections(): List<InspectionDto>? {
        return database.inspectionDao().getAllInspections()
    }

    suspend fun clearInspections() {
        database.inspectionDao().clearInspectionsTable()
    }

    suspend fun updateInspection(inspectionDto: InspectionDto) {
        database.inspectionDao().updateInspection(inspectionDto)
    }

    fun storeTokens(it: LoginResponseModel) {
        AppPreferenceHelper.auth = it.token
        AppPreferenceHelper.hash = it.uniqueHash
    }
}