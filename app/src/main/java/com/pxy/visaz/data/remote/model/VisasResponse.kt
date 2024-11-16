package com.pxy.visaz.data.remote.model

import com.google.gson.annotations.SerializedName


data class VisasResponse(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("banner_image_url") var bannerImageUrl: String? = null,
    @SerializedName("visa_info") var visaInfo: VisaInfo? = VisaInfo()

)