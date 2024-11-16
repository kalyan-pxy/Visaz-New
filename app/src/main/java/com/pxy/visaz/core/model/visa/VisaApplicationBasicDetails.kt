package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisaApplicationBasicDetails(
    val selectedDate: String,
    val motherName: String,
    val fatherName: String,
    val gender: String,
    val country: String
): Parcelable
