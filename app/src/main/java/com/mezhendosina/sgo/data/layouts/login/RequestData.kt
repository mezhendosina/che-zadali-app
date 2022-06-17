package com.mezhendosina.sgo.data.layouts.login


import com.google.gson.annotations.SerializedName

data class RequestData(
    @SerializedName("warnType")
    val warnType: String
)