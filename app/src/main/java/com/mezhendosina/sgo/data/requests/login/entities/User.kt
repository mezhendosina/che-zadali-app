package com.mezhendosina.sgo.data.requests.login.entities


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)