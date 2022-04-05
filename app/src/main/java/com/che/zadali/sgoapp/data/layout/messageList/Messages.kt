package com.che.zadali.sgoapp.data.layout.messageList


import com.google.gson.annotations.SerializedName

data class Messages(
    @SerializedName("Records")
    val records: List<Record>,
    @SerializedName("Result")
    val result: String,
    @SerializedName("ResultStatus")
    val resultStatus: Int,
    @SerializedName("TotalRecordCount")
    val totalRecordCount: Int
)