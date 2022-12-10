package com.mezhendosina.sgo.data.requests.sgo.login.entities


import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)