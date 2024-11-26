package com.pxy.visaz.data.mapper

import com.google.gson.Gson
import com.pxy.visaz.core.model.ErrorModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.core.model.visa.VisaFee
import com.pxy.visaz.core.model.visa.VisaType
import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.remote.model.Fees
import com.pxy.visaz.data.remote.model.InspectionDetailsModel
import com.pxy.visaz.data.remote.model.VisaInfo
import com.pxy.visaz.data.remote.model.VisasResponse
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.InspectionHeaderOptions
import com.pxy.visaz.domain.model.InspectionItemType
import com.pxy.visaz.domain.model.response.ErrorResponse
import okhttp3.ResponseBody
import okhttp3.internal.toImmutableList

fun InspectionDetailsModel.toDto(key: String): InspectionDto =
    InspectionDto(
        sno = sno,
        details = details,
        required = required,
        observation = observation,
        remarks = remarks,
        sheetId = key
    )

fun InspectionDetails.toDto(): InspectionDto =
    InspectionDto(
        sno = sno.toInt(),
        details = details,
        required = required,
        observation = observation,
        remarks = remarks,
        sheetId = sheetGroupName
    )

fun InspectionDto.toInspectionDetails(): InspectionDetails =
    InspectionDetails(
        sno = sno.toString(),
        details = details,
        required = required,
        observation = observation,
        remarks = remarks,
        sheetGroupName = sheetId,
        inspectionItemType = InspectionItemType.LIST_ITEM
    )

fun convertToInspectionMap(localInspectionDetails: List<InspectionDto>?): LinkedHashMap<String, MutableList<InspectionDetails>>? {
    val convertedMap: LinkedHashMap<String, MutableList<InspectionDetails>>? =
        localInspectionDetails?.groupBy {
            it.sheetId
        }?.mapNotNull { entry ->
            entry.key?.let { key ->
                val inspectionDetailsList = entry.value.map { dto ->
                    // Convert InspectionDto to InspectionDetails
                    dto.toInspectionDetails()
                }
                key to (inspectionDetailsList).toMutableList().apply {
                    add(0, inspectionDetailsHeader)
                }
            }
        }?.toMap(LinkedHashMap())
    return convertedMap
}

val inspectionDetailsHeader = InspectionDetails(
    sno = "Sno",
    details = "Details",
    required = "Required",
    observation = "Observation",
    remarks = "Remarks",
    inspectionItemType = InspectionItemType.LIST_HEADER
)

val inspectionHeaderOptions = InspectionHeaderOptions(
    option1 = "Drawing No : M314.001230 - 03",
    option2 = "Sr.No.of Component : Y-24-5100",
    option3 = "Description : S144 MAIN FRAME",
    option4 = "Stage of Inspection : Machined"
)

fun ResponseBody.errorModel(): ErrorModel {
    val errorResponse = Gson().fromJson(this.string(), ErrorResponse::class.java)
    return ErrorModel(
        errorCode = this.hashCode(),
        errorMessage = errorResponse.message.orEmpty()
    )
}

fun generateErrorModel(code: Int, errorBody: ResponseBody?): ErrorModel {
    var errorMessage = "Something went wrong. Please try again."
    errorBody?.string()?.let { errorJsonString ->
        if (errorJsonString.contains("message") || errorJsonString.contains("error")) {
            val errorResponse = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
            errorResponse?.let {
                errorMessage = if (it.error.isNullOrEmpty()) {
                    it.message ?: errorMessage
                } else {
                    it.error
                }
            }
        }
    }
    return ErrorModel(
        code,
        errorMessage
    )
}

fun mapVisas(visasResponse: List<VisasResponse>): List<VisaApplicationModel> {
    val visaApplicationModelList: ArrayList<VisaApplicationModel> = arrayListOf()

    visasResponse.toImmutableList().forEach {
        visaApplicationModelList.add(
            VisaApplicationModel(
                id = it.id.toString(),
                name = it.name.orEmpty(),
                description = it.description.orEmpty(),
                bannerImageUrl = it.bannerImageUrl.orEmpty(),
                visaTypes = it.visaInfo?.toVisaTypes()
            )
        )
    }
    return visaApplicationModelList
}

fun VisaInfo.toVisaTypes(): List<VisaType> {
    val list: ArrayList<VisaType> = arrayListOf()
    types.forEach {
        list.add(
            VisaType(
                fees = it.fees?.toVisaFee(),
                type = it.type,
                description = it.description,
                processingTime = it.processingTime
            )
        )
    }
    return list
}

fun Fees.toVisaFee(): VisaFee {
    return VisaFee(
        serviceFee = serviceFee,
        applicationFee = applicationFee
    )
}

