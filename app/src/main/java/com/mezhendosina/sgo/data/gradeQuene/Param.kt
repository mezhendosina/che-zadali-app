package com.mezhendosina.sgo.data.gradeQuene


import com.google.gson.annotations.SerializedName

data class Param(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Any
)