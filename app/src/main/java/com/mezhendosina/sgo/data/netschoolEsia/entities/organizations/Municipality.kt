package com.mezhendosina.sgo.data.netschoolEsia.entities.organizations


import com.google.gson.annotations.SerializedName

data class Municipality(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)