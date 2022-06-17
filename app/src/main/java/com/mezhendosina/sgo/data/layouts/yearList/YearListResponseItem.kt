package com.mezhendosina.sgo.data.layouts.yearList


import com.google.gson.annotations.SerializedName

data class YearListResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)