package com.pxy.visaz.data.remote.model

import com.google.gson.annotations.SerializedName


data class VisaInfo(

    @SerializedName("types") var types: ArrayList<Types> = arrayListOf(),
    @SerializedName("overview") var overview: String? = null

)