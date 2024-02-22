package com.mezhendosina.sgo.data.netschoolEsia.entities.analytics


import com.google.gson.annotations.SerializedName

data class ClassmeetingsStats(
    @SerializedName("passed")
    val passed: Int,
    @SerializedName("scheduled")
    val scheduled: Int
)