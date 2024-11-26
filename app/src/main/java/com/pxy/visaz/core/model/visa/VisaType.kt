package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class VisaType(
    var fees: VisaFee? = null,
    var type: String? = null,
    var description: String? = null,
    var processingTime: String? = null,
    var isSelected: Boolean = false
): Parcelable
