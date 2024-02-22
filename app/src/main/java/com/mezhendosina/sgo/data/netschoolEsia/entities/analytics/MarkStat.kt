package com.mezhendosina.sgo.data.netschoolEsia.entities.analytics


import com.google.gson.annotations.SerializedName

data class MarkStat(
    @SerializedName("count")
    val count: Int,
    @SerializedName("fraction")
    val fraction: Double,
    @SerializedName("mark")
    val mark: Double
)