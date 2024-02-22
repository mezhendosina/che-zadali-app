package com.mezhendosina.sgo.data.netschoolEsia.entities.common


import com.google.gson.annotations.SerializedName

data class Subject(
    // here, in word 'Curriculum' (in annotation) it looks like that in JSON response from server
    // developers used non-ascii 'C' character
    @SerializedName("federalСurriculum")
    val federalCurriculum: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int
)