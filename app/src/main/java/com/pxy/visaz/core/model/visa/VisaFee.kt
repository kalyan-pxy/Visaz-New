package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class VisaFee(
    var serviceFee: Int? = null,
    var applicationFee: Int? = null
): Parcelable
