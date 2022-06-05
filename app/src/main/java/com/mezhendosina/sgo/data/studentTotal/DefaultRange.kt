package com.mezhendosina.sgo.data.studentTotal


import com.google.gson.annotations.SerializedName

data class DefaultRange(
    @SerializedName("end")
    val end: String,
    @SerializedName("start")
    val start: String
)