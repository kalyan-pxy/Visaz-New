package com.pxy.visaz.data.repository


import com.pxy.visaz.core.model.BaseModel
import com.pxy.visaz.core.model.CreatePasswordModel
import com.pxy.visaz.core.model.ErrorModel
import com.pxy.visaz.core.model.LoginModel
import com.pxy.visaz.core.model.SignUpModel
import com.pxy.visaz.core.model.User
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.local.AppPreferenceHelper
import com.pxy.visaz.data.local.LocalDataSource
import com.pxy.visaz.data.mapper.convertToInspectionMap
import com.pxy.visaz.data.mapper.generateErrorModel
import com.pxy.visaz.data.mapper.inspectionHeaderOptions
import com.pxy.visaz.data.mapper.mapVisas
import com.pxy.visaz.data.mapper.toDto
import com.pxy.visaz.data.remote.RemoteDataSource
import com.pxy.visaz.domain.IRepository
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.Inspections
import com.pxy.visaz.domain.model.request.CreatePasswordRequestModel
import com.pxy.visaz.domain.model.request.LoginRequestModel
import com.pxy.visaz.domain.model.request.SignUpRequestModel

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {
    override suspend fun refreshInspections(): Inspections {
        val localInspections: ArrayList<InspectionDto> = arrayListOf()
        val result = remoteDataSource.appService.getInspections()

        result.body()?.inspections?.forEach { inspectionSection ->
            val section = inspectionSection.inspectionSection
            inspectionSection.inspectionDetails.forEach {
                localInspections.add(it.toDto(section))
            }
        }
        if (localInspections.isNotEmpty()) {
            localDataSource.clearInspections()
            localDataSource.storeInspections(localInspections)
        }
        val localInspectionDetails = localDataSource.retrieveInspections()
        return Inspections(
            convertToInspectionMap(localInspectionDetails),
            inspectionHeaderOptions
        )
    }

    override suspend fun getInspections(): Inspections {
        val inspections = Inspections()
        val localInspectionList = localDataSource.retrieveInspections()
        if (localInspectionList.isNullOrEmpty().not()) {
            inspections.inspectionDetails = convertToInspectionMap(localInspectionList)
            inspections.inspectionHeaderOptions = inspectionHeaderOptions
            return inspections
        }
        //if local data is not there
        val localInspections: ArrayList<InspectionDto> = arrayListOf()
        val result = remoteDataSource.appService.getInspections()
        if (result.isSuccessful) {
            result.body()?.inspections?.forEach { inspectionSection ->
                val section = inspectionSection.inspectionSection
                inspectionSection.inspectionDetails.forEach {
                    localInspections.add(it.toDto(section))
                }
            }
            if (localInspections.isNotEmpty()) {
                localDataSource.storeInspections(localInspections)
            }
            val localInspectionDetails = localDataSource.retrieveInspections()
            inspections.inspectionDetails = convertToInspectionMap(localInspectionDetails)
            inspections.inspectionHeaderOptions = inspectionHeaderOptions
        }
        return inspections
    }

    override suspend fun updateInspection(inspectionDetails: InspectionDetails): InspectionDetails {
        localDataSource.updateInspection(
            inspectionDetails.toDto()
        )
        return InspectionDetails(
            sno = inspectionDetails.sno,
            details = inspectionDetails.details,
            required = inspectionDetails.required,
            observation = inspectionDetails.observation,
            remarks = inspectionDetails.remarks
        )
    }

    override suspend fun login(loginRequestModel: LoginRequestModel): BaseModel<LoginModel> {
        val result = remoteDataSource.appService.login(loginRequestModel)
        return if (result.isSuccessful) {
            AppPreferenceHelper.user = User(
                loginRequestModel.email
            )
            result.body()?.let {
                localDataSource.storeTokens(it)
            }
            BaseModel(
                isSuccessful = true,
                model = LoginModel(true, result.body()?.message.orEmpty())
            )
        } else {
            BaseModel(
                isSuccessful = false,
                errorModel = generateErrorModel(result.code(), result.errorBody())
            )
        }
    }

    override suspend fun signup(signUpRequestModel: SignUpRequestModel): BaseModel<SignUpModel> {
        val result = remoteDataSource.appService.signUp(signUpRequestModel)
        return if (result.isSuccessful) {
            BaseModel(
                isSuccessful = true,
                model = SignUpModel(true, result.body()?.message.orEmpty())
            )
        } else {
            BaseModel(
                isSuccessful = false,
                errorModel = generateErrorModel(result.code(), result.errorBody())
            )
        }
    }

    override suspend fun createPassword(createPasswordRequestModel: CreatePasswordRequestModel): BaseModel<CreatePasswordModel> {
        val result = remoteDataSource.appService.createPassword(createPasswordRequestModel)
        return if (result.isSuccessful) {
            BaseModel(
                isSuccessful = true,
                model = CreatePasswordModel(true, result.body()?.message.orEmpty())
            )
        } else {
            BaseModel(
                isSuccessful = false,
                errorModel = generateErrorModel(result.code(), result.errorBody())
            )
        }
    }

    override fun isLoggedInUser(): Boolean {
        AppPreferenceHelper.user?.let {
            return true
        } ?: run {
            return false
        }
    }

    override suspend fun loadVisas(): BaseModel<List<VisaApplicationModel>> {
        val result = remoteDataSource.appService.getVisaCountries()
        //val result = visaApplicationList
        result.body()?.let {
            return BaseModel(
                isSuccessful = true,
                model = mapVisas(it)
            )
        }
        return BaseModel(
            isSuccessful = false,
            errorModel = ErrorModel(
                result.code(),
                "Something went wrong"
            )
        )
    }
}
