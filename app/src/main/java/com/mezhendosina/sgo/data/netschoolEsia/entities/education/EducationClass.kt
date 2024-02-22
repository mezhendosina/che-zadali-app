package com.mezhendosina.sgo.data.netschoolEsia.entities.education


import com.google.gson.annotations.SerializedName

data class EducationClass(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isFree")
    val isFree: Boolean,
    @SerializedName("name")
    val name: String
)