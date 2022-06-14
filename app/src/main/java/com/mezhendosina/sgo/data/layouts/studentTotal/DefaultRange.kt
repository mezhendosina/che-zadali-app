package com.mezhendosina.sgo.data.layouts.studentTotal


import com.google.gson.annotations.SerializedName

data class DefaultRange(
    @SerializedName("end")
    val end: String,
    @SerializedName("start")
    val start: String
)