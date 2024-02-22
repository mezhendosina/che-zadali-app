package com.mezhendosina.sgo.data.netschoolEsia.entities.announcements


import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)