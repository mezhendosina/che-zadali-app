package com.mezhendosina.sgo.data.requests.sgo.login.entities


import com.google.gson.annotations.SerializedName

data class RequestData(
    @SerializedName("warnType")
    val warnType: String
)