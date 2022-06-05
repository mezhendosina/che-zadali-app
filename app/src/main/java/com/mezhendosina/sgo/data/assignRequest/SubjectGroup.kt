package com.mezhendosina.sgo.data.assignRequest


import com.google.gson.annotations.SerializedName

data class SubjectGroup(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)