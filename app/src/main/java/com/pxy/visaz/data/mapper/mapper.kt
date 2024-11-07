package com.pxy.visaz.data.mapper

import com.google.gson.Gson
import com.pxy.visaz.core.model.ErrorModel
import com.pxy.visaz.core.model.VisaModel
import com.pxy.visaz.core.model.visa.ApplicationFeeModel
import com.pxy.visaz.core.model.visa.VisaApplicationModel
import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.remote.model.InspectionDetailsModel
import com.pxy.visaz.domain.model.InspectionDetails
import com.pxy.visaz.domain.model.InspectionHeaderOptions
import com.pxy.visaz.domain.model.InspectionItemType
import com.pxy.visaz.domain.model.response.ErrorResponse
import okhttp3.ResponseBody

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

val visaApplicationList = listOf(
    VisaApplicationModel(
        visaType = "Work Visa",
        country = "USA",
        imageUrl = "https://housing.com/news/wp-content/uploads/2022/10/Places-to-Visit-in-America.jpg",
        requirements = listOf(
            "Valid passport",
            "Job offer letter",
            "Completed visa application form",
            "Proof of financial support"
        ),
        processingTime = "10 working days",
        validityPeriod = "2 years",
        applyLink = "https://example.com/apply/usa-work-visa",
        contactInformation = "info@usa-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 15000.0,
            companyFeeActual = 4000.0,
            companyFeeNow = 0.0,
            note = "Company fee includes expedited processing."
        ),
        getOnDate = "10 Feb 2024"
    ),
    VisaApplicationModel(
        visaType = "Business Visa",
        country = "Canada",
        imageUrl = "https://dooktravels.s3.ap-south-1.amazonaws.com/com/poi/66ab888070943.webp",
        requirements = listOf(
            "Valid passport",
            "Business invitation letter",
            "Completed visa application form",
            "Proof of accommodation"
        ),
        processingTime = "12 working days",
        validityPeriod = "6 months",
        applyLink = "https://example.com/apply/canada-business-visa",
        contactInformation = "info@canada-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 10000.0,
            companyFeeActual = 3000.0,
            companyFeeNow = 3000.0,
            note = "Fees may vary depending on the service level."
        ),
        getOnDate = "12 Oct 2023"
    ),
    VisaApplicationModel(
        visaType = "Business Visa",
        country = "Australia",
        imageUrl = "https://www.planetware.com/photos-large/AUS/australia-new-south-wales-sydney-harbour-bridge.jpg",
        requirements = listOf(
            "Valid passport",
            "Business invitation letter",
            "Completed visa application form",
            "Proof of financial support"
        ),
        processingTime = "14 working days",
        validityPeriod = "12 months",
        applyLink = "https://example.com/apply/australia-business-visa",
        contactInformation = "info@australia-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 12000.0,
            companyFeeActual = 3500.0,
            companyFeeNow = 3500.0,
            note = "Additional charges may apply for urgent processing."
        ),
        getOnDate = "25 Nov 2023"
    ),
    VisaApplicationModel(
        visaType = "Tourist Visa",
        country = "UK",
        imageUrl = "https://www.godigit.com/content/dam/godigit/directportal/en/best-places-to-visit-in-united-kingdom.jpg",
        requirements = listOf(
            "Valid passport",
            "Completed visa application form",
            "Proof of accommodation",
            "Travel itinerary"
        ),
        processingTime = "15 working days",
        validityPeriod = "6 months",
        applyLink = "https://example.com/apply/uk-tourist-visa",
        contactInformation = "info@uk-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 9000.0,
            companyFeeActual = 5000.0,
            companyFeeNow = 4000.0,
            note = "Visa fees may vary depending on nationality."
        ),
        getOnDate = "05 Dec 2023"
    ),
    VisaApplicationModel(
        visaType = "Student Visa",
        country = "New Zealand",
        imageUrl = "https://www.celebritycruises.com/blog/content/uploads/2023/04/landmark-in-new-zealand-sky-tower-auckland-north-island-1024x683.jpg",
        requirements = listOf(
            "Valid passport",
            "Offer letter from New Zealand university",
            "Proof of financial support",
            "Completed visa application form"
        ),
        processingTime = "20 working days",
        validityPeriod = "1 year",
        applyLink = "https://example.com/apply/new-zealand-student-visa",
        contactInformation = "info@nz-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 20000.0,
            companyFeeActual = 5000.0,
            companyFeeNow = 5000.0,
            note = "Service fee includes document verification."
        ),
        getOnDate = "18 Jan 2024"
    ),
    VisaApplicationModel(
        visaType = "Work Visa",
        country = "Singapore",
        imageUrl = "https://www.holidify.com/images/bgImages/SINGAPORE.jpg",
        requirements = listOf(
            "Valid passport",
            "Job offer letter",
            "Proof of financial support",
            "Completed visa application form"
        ),
        processingTime = "8 working days",
        validityPeriod = "2 years",
        applyLink = "https://example.com/apply/singapore-work-visa",
        contactInformation = "info@singapore-visa.com",
        applicationFee = ApplicationFeeModel(
            visaFee = 13000.0,
            companyFeeActual = 4500.0,
            companyFeeNow = 4500.0,
            note = "Includes express processing service."
        ),
        getOnDate = "10 Feb 2024"
    )
)
