package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisaApplicationModel(
    val id: String,               // Type of visa (e.g., Tourist, Business)
    val name: String,                // Country for which the visa is applicable
    val bannerImageUrl: String? = null,      // URL of the country's special place image
    val description: String? = null,
    val visaTypes: List<VisaType>? = null,
) : Parcelable
