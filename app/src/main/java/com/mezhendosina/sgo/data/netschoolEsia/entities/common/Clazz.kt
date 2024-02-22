package com.mezhendosina.sgo.data.netschoolEsia.entities.common


import com.google.gson.annotations.SerializedName

data class Clazz(
    @SerializedName("classId")
    val classId: Int,
    @SerializedName("className")
    val className: String
)