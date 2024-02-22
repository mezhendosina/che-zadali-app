package com.mezhendosina.sgo.data.netschoolEsia.entities.users


import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("id")
    val id: Int,
    @SerializedName("isAddSchool")
    val isAddSchool: Boolean,
    @SerializedName("name")
    val name: String
)