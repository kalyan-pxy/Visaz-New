package com.pxy.visaz.domain.interactors

import com.pxy.visaz.core.model.BaseModel
import com.pxy.visaz.core.model.CreatePasswordModel
import com.pxy.visaz.core.model.LoginModel
import com.pxy.visaz.core.model.SignUpModel
import com.pxy.visaz.domain.IRepository
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.Inspections
import com.pxy.visaz.domain.model.request.CreatePasswordRequestModel
import com.pxy.visaz.domain.model.request.LoginRequestModel
import com.pxy.visaz.domain.model.request.SignUpRequestModel

class AuthUseCase(private val remoteRepository: IRepository) {
    suspend fun loadInspections(): Inspections {
        return remoteRepository.getInspections()
    }

    suspend fun refreshInspections(): Inspections {
        return remoteRepository.refreshInspections()
    }

    suspend fun updateInspection(inspectionDetails: InspectionDetails): InspectionDetails {
        return remoteRepository.updateInspection(inspectionDetails)
    }

    suspend fun login(userName: String, password: String): BaseModel<LoginModel> {
        return remoteRepository.login(
            LoginRequestModel(
                userName,
                password
            )
        )
    }

    suspend fun signUp(name: String, email: String, phone: String): BaseModel<SignUpModel> {
        return remoteRepository.signup(
            SignUpRequestModel(
                name = name,
                email = email,
                phone = phone
            )
        )
    }

    suspend fun createPassword(
        email: String,
        otp: String,
        password: String
    ): BaseModel<CreatePasswordModel> {
        return remoteRepository.createPassword(
            CreatePasswordRequestModel(
                email = email,
                otp = otp,
                password = password,
                confirm_password = password
            )
        )
    }
}