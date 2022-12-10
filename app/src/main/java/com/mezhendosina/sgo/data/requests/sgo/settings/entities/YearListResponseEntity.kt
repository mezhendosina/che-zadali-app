package com.mezhendosina.sgo.data.requests.sgo.settings.entities


import com.google.gson.annotations.SerializedName

data class YearListResponseEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)