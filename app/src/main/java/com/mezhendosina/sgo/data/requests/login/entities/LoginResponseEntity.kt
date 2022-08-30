package com.mezhendosina.sgo.data.requests.login.entities


import com.google.gson.annotations.SerializedName

data class LoginResponseEntity(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("accountInfo")
    val accountInfo: AccountInfo,
    @SerializedName("at")
    val at: String,
    @SerializedName("code")
    val code: Any,
    @SerializedName("entryPoint")
    val entryPoint: String,
    @SerializedName("errorMessage")
    val errorMessage: Any,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("requestData")
    val requestData: RequestData,
    @SerializedName("timeOut")
    val timeOut: Int,
    @SerializedName("tokenType")
    val tokenType: String
)