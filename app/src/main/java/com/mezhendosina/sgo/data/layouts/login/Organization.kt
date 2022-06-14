package com.mezhendosina.sgo.data.layouts.login


import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)