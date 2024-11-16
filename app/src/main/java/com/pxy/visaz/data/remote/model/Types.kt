package com.pxy.visaz.data.remote.model

import com.google.gson.annotations.SerializedName


data class Types(

    @SerializedName("fees") var fees: Fees? = Fees(),
    @SerializedName("type") var type: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("requirements") var requirements: ArrayList<String> = arrayListOf(),
    @SerializedName("processing_time") var processingTime: String? = null

)