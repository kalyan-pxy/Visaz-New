package com.pxy.visaz.ui

import com.pxy.visaz.domain.model.InspectionDetails

data class InspectionDetailsUpdateModel(
    val groupPosition: Int,
    val childPosition: Int,
    val inspectionDetails: InspectionDetails
)