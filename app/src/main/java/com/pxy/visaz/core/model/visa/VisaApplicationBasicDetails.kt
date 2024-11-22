package com.pxy.visaz.core.model.visa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisaApplicationBasicDetails(
    val title: String? = null,
    var selectedDate: String? = null,
    var motherName: String? = null,
    var fatherName: String? = null,
    var gender: String? = null,
    var country: String? = null,
    var profileImageUri: String? = null,
    var passportImageUri: String? = null
) : Parcelable
