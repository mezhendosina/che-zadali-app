package com.mezhendosina.sgo.data.layouts.preLoginNotice


import com.google.gson.annotations.SerializedName

data class PreLoginNoticeResponse(
    @SerializedName("enablePopUp")
    val enablePopUp: Boolean,
    @SerializedName("popUpButtonText")
    val popUpButtonText: String,
    @SerializedName("popUpDisplayText")
    val popUpDisplayText: String,
    @SerializedName("popUpEndDate")
    val popUpEndDate: String,
    @SerializedName("popUpStartDate")
    val popUpStartDate: String,
    @SerializedName("showPopUp")
    val showPopUp: Boolean
)