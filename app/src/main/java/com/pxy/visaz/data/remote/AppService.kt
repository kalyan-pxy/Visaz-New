package com.pxy.visaz.data.remote

import com.pxy.visaz.domain.model.request.SubmitVisaApplicationRequestModel
import com.pxy.visaz.data.remote.model.InspectionResponse
import com.pxy.visaz.data.remote.model.VisasResponse
import com.pxy.visaz.domain.ApiConstants
import com.pxy.visaz.domain.model.request.CreatePasswordRequestModel
import com.pxy.visaz.domain.model.request.LoginRequestModel
import com.pxy.visaz.domain.model.request.SignUpRequestModel
import com.pxy.visaz.domain.model.response.CreatePasswordResponseModel
import com.pxy.visaz.domain.model.response.LoginResponseModel
import com.pxy.visaz.domain.model.response.SignUpResponseModel
import com.pxy.visaz.domain.model.response.SubmitVisaApplicationResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppService {
    //https://mocki.io/v1/b086352c-70ce-4312-a203-a54b623f81b5
    @GET("inspections")
    suspend fun getInspections(): Response<InspectionResponse>

    @POST(ApiConstants.LOGIN_ENDPOINT)
    suspend fun login(
        @Body loginRequestModel: LoginRequestModel
    ): Response<LoginResponseModel>

    @POST(ApiConstants.SIGN_UP_ENDPOINT)
    suspend fun signUp(
        @Body singUpRequestModel: SignUpRequestModel
    ): Response<SignUpResponseModel>

    @POST(ApiConstants.CREATE_PASSWORD_ENDPOINT)
    suspend fun createPassword(
        @Body createPasswordRequestModel: CreatePasswordRequestModel
    ): Response<CreatePasswordResponseModel>

    @GET(ApiConstants.COUNTRIES_ENDPOINT)
    suspend fun getVisaCountries(
    ): Response<List<VisasResponse>>

    @POST(ApiConstants.VISA_SUBMIT_APPLICATION_ENDPOINT)
    suspend fun submitVisaApplication(
        @Body request: SubmitVisaApplicationRequestModel
    ): Response<SubmitVisaApplicationResponseModel>
}