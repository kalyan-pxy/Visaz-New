package com.pxy.visaz.domain.model.request

import android.os.Parcelable
import com.pxy.visaz.core.model.visa.VisaApplicationDetails
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class SubmitVisaApplicationRequestModel(
    val country: String,
    val visaType: String,
    val visaApplicationDetails: ArrayList<VisaApplicationDetails>
) : Parcelable
