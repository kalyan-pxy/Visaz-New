package com.pxy.visaz.domain

import com.pxy.visaz.core.model.BaseModel
import com.pxy.visaz.core.model.CreatePasswordModel
import com.pxy.visaz.core.model.LoginModel
import com.pxy.visaz.core.model.SignUpModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.Inspections
import com.pxy.visaz.domain.model.request.CreatePasswordRequestModel
import com.pxy.visaz.domain.model.request.LoginRequestModel
import com.pxy.visaz.domain.model.request.SignUpRequestModel

interface IRepository {
    suspend fun getInspections(): Inspections
    suspend fun refreshInspections(): Inspections
    suspend fun updateInspection(inspectionDetails: InspectionDetails): InspectionDetails

    suspend fun login(loginRequestModel: LoginRequestModel): BaseModel<LoginModel>
    suspend fun signup(signUpRequestModel: SignUpRequestModel): BaseModel<SignUpModel>
    suspend fun createPassword(createPasswordRequestModel: CreatePasswordRequestModel): BaseModel<CreatePasswordModel>
    fun isLoggedInUser(): Boolean
    suspend fun loadVisas(): BaseModel<List<VisaApplicationModel>>
}