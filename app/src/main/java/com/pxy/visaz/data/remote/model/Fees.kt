package com.pxy.visaz.data.remote.model

import com.google.gson.annotations.SerializedName


data class Fees(

    @SerializedName("service_fee") var serviceFee: Int? = null,
    @SerializedName("application_fee") var applicationFee: Int? = null

)