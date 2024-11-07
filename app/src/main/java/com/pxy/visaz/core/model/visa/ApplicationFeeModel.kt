package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApplicationFeeModel(
    val visaFee: Double,              // Fee for the visa application
    val companyFeeActual: Double,     // Actual company fee for processing
    val companyFeeNow: Double,         // Current company fee for processing
    val note: String? = null           // Any additional notes regarding the fees
): Parcelable