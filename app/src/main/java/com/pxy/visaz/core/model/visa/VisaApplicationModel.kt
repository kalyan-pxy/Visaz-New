package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisaApplicationModel(
    val visaType: String,               // Type of visa (e.g., Tourist, Business)
    val country: String,                // Country for which the visa is applicable
    val imageUrl: String? = null,      // URL of the country's special place image
    val requirements: List<String>,     // List of requirements needed for the visa
    val processingTime: String,         // Estimated time for processing the visa application
    val validityPeriod: String,         // Duration for which the visa is valid (e.g., 30 days, 90 days)
    val applicationFee: ApplicationFeeModel, // Fee details
    val getOnDate: String,
    val description: String? = null
): Parcelable