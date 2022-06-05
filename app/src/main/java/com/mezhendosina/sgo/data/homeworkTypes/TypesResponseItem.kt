package com.mezhendosina.sgo.data.homeworkTypes


import com.google.gson.annotations.SerializedName

data class TypesResponseItem(
    @SerializedName("abbr")
    val abbr: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int
)