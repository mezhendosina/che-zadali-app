package com.mezhendosina.sgo.data.requests.sgo.diary.entities


import com.google.gson.annotations.SerializedName

data class AssignmentTypesResponseEntity(
    @SerializedName("abbr")
    val abbr: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int
)