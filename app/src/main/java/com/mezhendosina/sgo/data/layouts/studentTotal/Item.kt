package com.mezhendosina.sgo.data.layouts.studentTotal


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("title")
    val title: String,
    @SerializedName("value")
    val value: String
)