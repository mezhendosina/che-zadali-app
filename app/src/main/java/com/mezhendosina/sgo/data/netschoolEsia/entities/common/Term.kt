package com.mezhendosina.sgo.data.netschoolEsia.entities.common


import com.google.gson.annotations.SerializedName

data class Term(
    @SerializedName("current")
    val current: Boolean,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("type")
    val type: Type
)