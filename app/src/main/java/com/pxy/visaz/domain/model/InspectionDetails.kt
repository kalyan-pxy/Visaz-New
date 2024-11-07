package com.pxy.visaz.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class InspectionDetails(
    val sno: String,
    val details: String? = null,
    val required: String? = null,
    val observation: String? = null,
    val remarks: String? = null,
    val sheetGroupName: String? = null,
    var inspectionItemType: InspectionItemType = InspectionItemType.GROUP_HEADER
): Parcelable

@Keep
enum class InspectionItemType {
    GROUP_HEADER,
    LIST_HEADER,
    LIST_ITEM
}

@Keep
data class Inspections(
    var inspectionDetails: HashMap<String, MutableList<InspectionDetails>>? = null,
    var inspectionHeaderOptions: InspectionHeaderOptions? = null
)

@Keep
data class InspectionHeaderOptions(
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String
)

