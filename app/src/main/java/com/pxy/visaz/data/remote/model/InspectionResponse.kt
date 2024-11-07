package com.pxy.visaz.data.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class InspectionResponse(
    @SerializedName("inspections")
    val inspections: ArrayList<InspectionSection>
)

@Keep
data class InspectionSection(
    @SerializedName("inspection_section")
    val inspectionSection: String,
    @SerializedName("inspection_details")
    val inspectionDetails: ArrayList<InspectionDetailsModel>
)

@Keep
class InspectionDetailsModel(
    val sno: Int,
    val details: String,
    val required: String,
    val observation: String,
    val remarks: String
)