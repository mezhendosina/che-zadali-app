package com.mezhendosina.sgo.data.login


import com.google.gson.annotations.SerializedName

data class RequestData(
    @SerializedName("warnType")
    val warnType: String
)